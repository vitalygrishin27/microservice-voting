package com.voting.repository;

import com.voting.bom.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepo extends JpaRepository<Contest, Long> {
}


