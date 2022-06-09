package com.voting.service;

import com.voting.bom.Contest;
import com.voting.bom.Jury;
import com.voting.bom.Performance;

import java.util.List;

public interface ClientService {
    String loginIn(Jury jury);

    Performance getActivePerformanceIfChanged(Long contestId, Long previousPerformanceId, String token);

    List<Contest> getAvailableContests(String token);

    Performance createMarks(Performance performance);
}
