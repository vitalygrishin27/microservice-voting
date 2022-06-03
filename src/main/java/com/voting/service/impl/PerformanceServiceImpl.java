package com.voting.service.impl;

import com.voting.bom.Contest;
import com.voting.bom.Member;
import com.voting.bom.Performance;
import com.voting.exception.ContestException;
import com.voting.exception.PerformanceException;
import com.voting.repository.PerformanceRepo;
import com.voting.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceServiceImpl implements PerformanceService {
    @Autowired
    PerformanceRepo repo;

    @Override
    public Long create(Performance performance) {
        repo.saveAndFlush(performance);
        return performance.getId();
    }

    @Override
    public void deleteAllByMember(Member member) {
        repo.deleteAllByMember(member);
    }

    @Override
    public Performance getById(Long performanceId) {
        return repo.findById(performanceId).get();
    }

    @Override
    public List<Performance> getByMember(Member member) {
        return repo.findByMember(member);
    }

    @Override
    public List<Performance> getByContest(Contest contest) {
        return repo.findByContest(contest);
    }

    @Override
    public void updateAll(List<Performance> performances) {
        performances.forEach(performance -> {
            Performance performanceFromDB = getPerformanceIfExists(performance.getId());
            performanceFromDB.setTurnNumber(performance.getTurnNumber());
            repo.saveAndFlush(performanceFromDB);
        });
    }

    public Performance getPerformanceIfExists(Long performanceId) {
        Optional<Performance> optionalPerformance = repo.findById(performanceId);
        if (optionalPerformance.isEmpty()) {
            throw new PerformanceException(String.format("Performance with id=%s not found", performanceId));
        }
        return optionalPerformance.get();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
