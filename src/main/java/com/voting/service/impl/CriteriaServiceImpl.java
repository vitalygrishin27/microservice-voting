package com.voting.service.impl;

import com.voting.bom.Criteria;
import com.voting.exception.CriteriaException;
import com.voting.repository.CriteriaRepo;
import com.voting.service.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CriteriaServiceImpl implements CriteriaService {
    @Autowired
    CriteriaRepo repo;

    @Override
    public Long create(Criteria criteria) {
        if (criteria.getName().equals("")) throw new CriteriaException("Criteria name cannot be null");
        try {
            repo.saveAndFlush(criteria);
        } catch (Exception e) {
            throw new CriteriaException(String.format("DB exception. Criteria with name=%s already exist", criteria.getName()));
        }
        return criteria.getId();
    }

    @Override
    public void delete(Long criteriaId) {
        repo.delete(getCriteriaIfExists(criteriaId));
    }

    @Override
    public Long update(Criteria criteria) {
        if (criteria.getName().equals("")) throw new CriteriaException("Criteria name cannot be null");
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        Example<Criteria> example = Example.of(new Criteria(null, criteria.getName(), null, null), caseInsensitiveExampleMatcher);
        Optional<Criteria> actualOpt = repo.findOne(example);
        if (actualOpt.isPresent() && !Objects.equals(actualOpt.get().getId(), criteria.getId())) {
            throw new CriteriaException(String.format("Criteria with name=%s already exist", criteria.getName()));
        }
        Criteria actual = getCriteriaIfExists(criteria.getId());
        actual.setName(criteria.getName());
        actual.setDescription(criteria.getDescription());
        repo.saveAndFlush(actual);
        return actual.getId();
    }

    @Override
    public List<Criteria> getAll() {
        return repo.findAll();
    }

    @Override
    public Criteria findById(Long id) {
        Optional<Criteria> optionalCriteria = repo.findById(id);
        if (optionalCriteria.isEmpty()) throw new CriteriaException("Criteria with id=" + id + " not exists");
        return optionalCriteria.get();
    }

    private Criteria getCriteriaIfExists(Long criteriaId) {
        Optional<Criteria> optionalCriteria = repo.findById(criteriaId);
        if (optionalCriteria.isEmpty()) {
            throw new CriteriaException(String.format("Criteria with id=%s not found", criteriaId));
        }
        return optionalCriteria.get();
    }
}
