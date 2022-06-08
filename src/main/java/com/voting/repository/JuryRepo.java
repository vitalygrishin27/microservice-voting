package com.voting.repository;

import com.voting.bom.Jury;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuryRepo extends JpaRepository<Jury, Long> {
    Jury findByLogin(String login);
}


