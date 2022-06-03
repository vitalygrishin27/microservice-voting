package com.voting.service;

import com.voting.bom.Contest;
import com.voting.bom.Member;
import com.voting.bom.Performance;

import java.util.List;

public interface PerformanceService {

    Long create(Performance performance);

    void deleteAllByMember(Member member);

    Performance getById(Long performanceId);

    List<Performance> getByMember(Member member);

    List<Performance> getByContest(Contest contest);

    Performance getPerformanceIfExists(Long performanceId);

    void updateAll(List<Performance> performances);

    void deleteById(Long id);
}
