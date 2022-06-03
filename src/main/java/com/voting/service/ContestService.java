package com.voting.service;


import com.voting.bom.Contest;
import com.voting.bom.Performance;

import java.util.List;

public interface ContestService {
    Long create(Contest contest);

    void delete(Long contestId);

    Long update(Contest contest);

    List<Contest> getAll();

    List<Performance> getAllPerformancesForContest(Long contestId);

    Contest setActivePerformanceForContest(Long contestId, Performance performance);
}
