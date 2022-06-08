package com.voting.service;

import com.voting.bom.Jury;

import java.util.List;

public interface JuryService {
    Long create(Jury jury);

    void delete(Long criteriaId);

    Long update(Jury jury);

    List<Jury> getAll();

    Jury findByLogin(String login);

    boolean isPasswordMatch(String password, String encryptedPassword);

    Jury getJuryIfExists(Long juryId);
}
