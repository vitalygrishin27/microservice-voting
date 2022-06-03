package com.voting.repository;

import com.voting.bom.Contest;
import com.voting.bom.Member;
import com.voting.bom.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PerformanceRepo extends JpaRepository<Performance, Long> {
    @Transactional
    @Modifying
    @Query("delete from Performance p where p.member = :member")
    void deleteAllByMember(Member member);

    @Query("select p from Performance p where p.member = :member")
    List<Performance> findByMember(Member member);

    @Query("select p from Performance p where p.contest = :contest")
    List<Performance> findByContest(Contest contest);
}


