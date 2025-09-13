package com.custom.ngow.shop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.CategoryDto;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final MessageUtil messageUtil;

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategoryByCode(String categoryCode) {
    return categoryRepository
        .findByCode(categoryCode)
        .orElseThrow(
            () -> new CustomException(messageUtil, "", new String[] {"Code"}, "error.notExist"));
  }

  public Category getCategoryById(Long id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(
            () -> new CustomException(messageUtil, "", new String[] {"Id"}, "error.notExist"));
  }

  public void createCategory(CategoryDto categoryDto) {
    if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
      log.error("Category already exists with the name {}", categoryDto.getName());
      throw new CustomException(
          messageUtil, "name", new String[] {categoryDto.getName()}, "error.exist");
    }

    Category category = new Category();
    category.setName(categoryDto.getName());
    category.setCode(categoryDto.getCode());
    category.setDescription(categoryDto.getDescription());

    categoryRepository.save(category);
    log.info("Created category with name {}", categoryDto.getName());
  }

  public Page<CategoryDto> getCategories(int page, int size, String sortBy, String dir) {
    Sort.Direction direction =
        dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Category> categories = categoryRepository.findAll(pageable);

    return categories.map(
        category ->
            new CategoryDto(
                category.getId(),
                category.getName(),
                category.getCode(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()));
  }

  public long countAllCategories() {
    return categoryRepository.count();
  }
}
