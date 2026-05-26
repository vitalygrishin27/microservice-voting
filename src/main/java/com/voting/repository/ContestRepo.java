package com.voting.repository;

import com.voting.bom.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContestRepo extends JpaRepository<Contest, Long> {
    @Query("SELECT c FROM Contest c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Contest> findByName(@Param("name") String name);
}


