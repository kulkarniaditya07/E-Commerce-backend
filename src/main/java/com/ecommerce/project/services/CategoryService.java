package com.ecommerce.project.services;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.response.RestApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    RestApiResponse<Page<CategoryDTO>> getAllCategories(Pageable pageable);

    CategoryDTO createCategory(CategoryDTO categoryDto);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
