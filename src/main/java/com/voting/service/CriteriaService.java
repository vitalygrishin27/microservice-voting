package com.voting.service;

import com.voting.bom.Criteria;

import java.util.List;

public interface CriteriaService {
    Long create (Criteria criteria);

    void delete(Long criteriaId);

    Long update(Criteria criteria);

    List<Criteria> getAll();

    Criteria findById(Long id);
}
