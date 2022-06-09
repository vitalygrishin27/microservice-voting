package com.voting.repository;

import com.voting.bom.Jury;
import com.voting.bom.Mark;
import com.voting.bom.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MarkRepo extends JpaRepository<Mark, Long> {
    @Transactional
    @Modifying
    @Query("delete from Mark m where m.performance = :performance and m.jury = :jury")
    void deleteByPerformanceAndJury(Performance performance, Jury jury);
}


