package com.voting.repository;

import com.voting.bom.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CriteriaRepo extends JpaRepository<Criteria, Long> {
}


