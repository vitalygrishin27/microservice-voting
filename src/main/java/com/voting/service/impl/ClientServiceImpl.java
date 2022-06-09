package com.voting.service.impl;

import com.voting.bom.*;
import com.voting.exception.JuryException;
import com.voting.exception.PerformanceException;
import com.voting.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    JuryService juryService;

    @Autowired
    ContestService contestService;

    @Autowired
    PerformanceService performanceService;

    @Autowired
    CriteriaService criteriaService;

    @Autowired
    MarkService markService;

    @Autowired
    ConfigurationService configurationService;

    @Override
    public String loginIn(Jury jury) {
        if (jury.getToken() != null) {
            return checkToken(jury.getToken()).getParameter();
        }
        Jury juryDB = juryService.findByLogin(jury.getLogin());
        if (juryDB == null) throw new JuryException("Login is not exists");
        if (!juryService.isPasswordMatch(jury.getPassword(), juryDB.getEncryptedPassword()))
            throw new JuryException("Password is incorrect");

        Configuration configuration = new Configuration(
                null,
                "token",
                generateNewToken(),
                "Generated for jury with login=" + jury.getLogin() + " at " + LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(getExpirationValue()).format(formatter), juryDB.getId());
        configurationService.save(configuration);
        return configuration.getParameter();
    }

    private Configuration checkToken(String token) {
        Configuration configuration = configurationService.getConfiguration("token", token);
        if (configuration == null) throw new JuryException("Token not found");

        LocalDateTime expirationDateTime = LocalDateTime.parse(configuration.getValue(), formatter);
        if (expirationDateTime.isBefore(LocalDateTime.now())) throw new JuryException("Token is expired");
        //checking that token is related to existing jury
        juryService.getJuryIfExists(configuration.getEntityId());
        return configuration;
    }

    @Override
    public Performance getActivePerformanceIfChanged(Long contestId, Long previousPerformanceId, String token) {
        Configuration config = configurationService.getConfiguration(contestService.ACTIVE_PERFORMANCE_KEY, String.valueOf(contestId));
        if (config == null) {
            throw new PerformanceException("No new performance!");
        }
        //If active performance already in UI skip request
        if (Long.valueOf(config.getValue()).equals(previousPerformanceId)) return null;
        Jury jury = juryService.getJuryIfExists(checkToken(token).getEntityId());

        Performance performance = performanceService.getPerformanceIfExists(Long.valueOf(config.getValue()));
        //If all marks already exists Performance should not return to Jury
        if (performance.getCategory().getCriteria().size() == performance.getMarks().stream().filter(mark -> Objects.equals(mark.getJury().getId(), jury.getId())).count()) {
            throw new PerformanceException("All marks for this PerformanceId=" + performance.getId() + " already exists.");
        }

        contestService.fillInTransientFields(performance);
        return performance;
    }

    @Override
    public List<Contest> getAvailableContests(String token) {
        Configuration configuration = checkToken(token);
        return juryService.getJuryIfExists(configuration.getEntityId()).getContests();
    }

    @Override
    public Performance createMarks(Performance performance) {
        Configuration configuration = checkToken(performance.getToken());
        if (performance.getMarks().isEmpty()) throw new PerformanceException("No marks in request");
        if (performance.getCategory().getCriteria().size() != performance.getMarks().size())
            throw new PerformanceException("Not all marks received.");
        Jury jury = juryService.getJuryIfExists(configuration.getEntityId());
        markService.deleteAllByPerformanceAndJury(performance, jury);
        performance.getMarks().forEach(item -> {
            Mark mark = new Mark();
            mark.setCriteria(criteriaService.findById(item.getCriteriaId()));
            mark.setJury(jury);
            mark.setValue(item.getValue());
            mark.setPerformance(performanceService.getPerformanceIfExists(performance.getId()));
            markService.create(mark);
        });
        return performanceService.getPerformanceIfExists(performance.getId());
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private int getExpirationValue() {
        Configuration configExpiration = configurationService.getConfiguration("token", "expiration");
        if (configExpiration == null) {
            configExpiration = new Configuration(
                    null,
                    "token",
                    "expiration",
                    "Value in minutes for valid token. Default 600 minutes",
                    String.valueOf(600), -1L);
            configurationService.save(configExpiration);
            return 600;
        }
        return Integer.parseInt(configExpiration.getValue());
    }
}
