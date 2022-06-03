package com.voting.service;

import com.voting.bom.Category;

import java.util.List;

public interface CategoryService {
    Long create(Category category);

    void delete(Long categoryId);

    Long update(Category criteria);

    List<Category> getAll();

    Category getCategoryIfExists(Long categoryId);
}
