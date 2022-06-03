package com.voting.service.impl;

import com.voting.bom.Category;
import com.voting.exception.CategoryException;
import com.voting.repository.CategoryRepo;
import com.voting.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo repo;

    @Override
    public Long create(Category category) {
        if (category.getName().equals("")) throw new CategoryException("Category name cannot be null");
        try {
            checkUniqueName(category);
            repo.saveAndFlush(category);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return category.getId();
    }

    @Override
    public void delete(Long categoryId) {
        repo.delete(getCategoryIfExists(categoryId));
    }

    @Override
    public Long update(Category category) {
        if (category.getName().equals("")) throw new CategoryException("Category name cannot be null");

        repo.saveAndFlush(category);
        return category.getId();
    }

    @Override
    public List<Category> getAll() {
        return repo.findAll();
    }

    public Category getCategoryIfExists(Long categoryId) {
        Optional<Category> optionalCategory = repo.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryException(String.format("Category with id=%s not found", categoryId));
        }
        return optionalCategory.get();
    }

    private void checkUniqueName(Category category) {
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        Example<Category> example = Example.of(new Category(null, category.getName(), null, null, null), caseInsensitiveExampleMatcher);
        Optional<Category> actualOpt = repo.findOne(example);
        if (actualOpt.isPresent() && !Objects.equals(actualOpt.get().getId(), category.getId())) {
            throw new CategoryException(String.format("Category with name=%s already exist", category.getName()));
        }
    }
}
