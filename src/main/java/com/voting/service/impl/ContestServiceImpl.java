package com.voting.service.impl;

import com.voting.bom.*;
import com.voting.exception.CategoryException;
import com.voting.exception.ContestException;
import com.voting.exception.PerformanceException;
import com.voting.repository.ContestRepo;
import com.voting.service.ConfigurationService;
import com.voting.service.ContestService;
import com.voting.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    ContestRepo repo;

    @Autowired
    PerformanceService performanceService;

    @Autowired
    ConfigurationService configurationService;

    @Value("${maxUploadFileSizePlayerPhoto}")
    private Long maxUploadFileSizePhoto;

    @Override
    public Long create(Contest contest) {
        try {
            checkFieldsAndSetFile(contest);
            repo.saveAndFlush(contest);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return contest.getId();
    }

    @Override
    public void delete(Long contestId) {
        repo.delete(getContestIfExists(contestId));
    }

    @Override
    public Long update(Contest contest) {
        try {
            checkFieldsAndSetFile(contest);
            repo.saveAndFlush(contest);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return contest.getId();
    }

    @Override
    public List<Contest> getAll() {
        repo.findAll().forEach(contest -> {
            Configuration config = configurationService.getConfiguration(ACTIVE_PERFORMANCE_KEY, String.valueOf(contest.getId()));
            fillInTransientFields(contest);
            if (config != null) {
                Performance performance = null;
                try {
                    performance = performanceService.getPerformanceIfExists(Long.valueOf(config.getValue()));
                } catch (PerformanceException e) {
                    //Remove active performance from configuration because Performance not exists
                    configurationService.delete(config);
                }
                if (performance != null) {
                    fillInTransientFields(performance);
                    contest.setActivePerformance(performance);
                }
            }

        });
        return repo.findAll();
    }

    @Override
    public List<Performance> getAllPerformancesForContest(Long contestId) {
        List<Performance> result = performanceService.getByContest(getContestIfExists(contestId));
        result.sort(Performance.COMPARE_BY_TURN_NUMBER);
        fillInTransientFields(result);
        return result;
    }

    private void fillInTransientFields(List<Performance> performances) {
        performances.forEach(this::fillInTransientFields);
    }

    public void fillInTransientFields(Performance performance) {
        performance.setFullName(performance.getMember().getLastName() + " " +
                performance.getMember().getFirstName() + " " + performance.getMember().getSecondName());
        performance.setPlace(performance.getMember().getDescription());

        HashMap<Jury, Integer> summaryMarksMap = new HashMap<>();
        performance.getContest().getJuries().forEach(jury -> summaryMarksMap.put(jury, 0));

        performance.getMarks().forEach(mark -> {
            mark.setCriteriaName(mark.getCriteria().getName());
            mark.setJuryLastName(mark.getJury().getLastName());
            mark.setCriteriaId(mark.getCriteria().getId());
            summaryMarksMap.put(mark.getJury(), summaryMarksMap.getOrDefault(mark.getJury(), 0) + mark.getValue());
        });
        List<Mark> summaryMarks = new ArrayList<>();
        summaryMarksMap.forEach((key, value) -> {
            Mark mark = new Mark();
            mark.setJuryLastName(key.getLastName());
            mark.setValue(value);
            summaryMarks.add(mark);
        });
        performance.setSummaryMarks(summaryMarks);
    }

    @Override
    public Performance getUpdatedPerformanceData(Long contestId, Performance performanceFromUI) {
        Performance performanceFromDB = performanceService.getPerformanceIfExists(performanceFromUI.getId());
        fillInTransientFields(performanceFromDB);
        if (performanceFromUI.getSummaryMarks() == null) return performanceFromDB;
        HashMap<String, Integer> marksFromDB = new HashMap<>();
        performanceFromDB.getSummaryMarks().forEach(mark -> marksFromDB.put(mark.getJuryLastName(), mark.getValue()));

        HashMap<String, Integer> marksFromUI = new HashMap<>();
        performanceFromUI.getSummaryMarks().forEach(mark -> marksFromUI.put(mark.getJuryLastName(), mark.getValue()));

        AtomicBoolean needToUpdateUI = new AtomicBoolean(false);
        marksFromDB.forEach((key, value) -> {
            if (!marksFromUI.get(key).equals(value)) needToUpdateUI.set(true);

        });

        return needToUpdateUI.get() ? performanceFromDB : null;
    }

    @Override
    public Contest setActivePerformanceForContest(Long contestId, Performance performance) {
        Contest contest = getContestIfExists(contestId);
        fillInTransientFields(contest);
        Configuration config = configurationService.getConfiguration(ACTIVE_PERFORMANCE_KEY, String.valueOf(contest.getId()));
        if (config != null && performance == null) {
            configurationService.delete(config);
            return contest;
        }
        if (config == null && performance == null) {
            return contest;
        }
        if (config == null) config = new Configuration();
        config.setKey(ACTIVE_PERFORMANCE_KEY);
        config.setParameter(String.valueOf(contestId));
        config.setValue(String.valueOf(performance.getId()));
        config.setEntityId(performance.getId());
        configurationService.save(config);
        contest.setActivePerformance(performance);
        return contest;
    }

    private Contest getContestIfExists(Long contestId) {
        Optional<Contest> optionalContest = repo.findById(contestId);
        if (optionalContest.isEmpty()) {
            throw new ContestException(String.format("Contest with id=%s not found", contestId));
        }
        return optionalContest.get();
    }

    private void checkFieldsAndSetFile(Contest contest) throws IOException {
        if (contest.getName() == null ||
                contest.getName().equals("")) throw new ContestException("Name can`t be empty");

        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        Example<Contest> example = Example.of(new Contest(contest.getName()), caseInsensitiveExampleMatcher);
        Optional<Contest> actualOpt = repo.findOne(example);
        if (actualOpt.isPresent() && !Objects.equals(actualOpt.get().getId(), contest.getId())) {
            throw new ContestException(String.format("Contest with name=%s already exist", contest.getName()));
        }

        if (contest.getFile() != null && contest.getFile().getSize() > maxUploadFileSizePhoto)
            throw new ContestException("File size is too large. Allowed to " + maxUploadFileSizePhoto);
        if (contest.getFile() != null)
            contest.setPhoto("data:image/jpeg;base64, " + Base64Utils.encodeToString(contest.getFile().getBytes()));
    }

    private void fillInTransientFields(Contest contest) {
        List<String> juryLastNames = new ArrayList<>();
        contest.getJuries().forEach(jury -> {
            juryLastNames.add(jury.getLastName());
        });
        contest.setJuryLastNames(juryLastNames);
    }

}
