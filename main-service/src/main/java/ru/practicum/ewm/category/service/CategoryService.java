package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void delete(Long id);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long id);
}
