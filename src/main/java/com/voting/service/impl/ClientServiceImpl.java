package com.voting.service.impl;

import com.voting.bom.Configuration;
import com.voting.bom.Contest;
import com.voting.bom.Jury;
import com.voting.bom.Performance;
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
    public Performance getActivePerformanceIfChanged(Long contestId, Long previousPerformanceId) {
        Configuration config = configurationService.getConfiguration(contestService.ACTIVE_PERFORMANCE_KEY, String.valueOf(contestId));
        if (config == null) {
            throw new PerformanceException("No new performance!");
        }
        Performance performance = performanceService.getPerformanceIfExists(Long.valueOf(config.getValue()));
        contestService.fillInTransientFields(performance);
        return performance;
    }

    @Override
    public List<Contest> getAvailableContests(String token) {
        Configuration configuration = checkToken(token);
        return juryService.getJuryIfExists(configuration.getEntityId()).getContests();
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
