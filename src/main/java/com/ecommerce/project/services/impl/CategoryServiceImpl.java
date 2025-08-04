package com.ecommerce.project.services.impl;

import com.ecommerce.project.exceptions.RestApiException;
import com.ecommerce.project.entity.Category;
import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.services.CategoryService;
import com.ecommerce.project.common.PagableObject;
import com.ecommerce.project.common.ResponseUtil;
import com.ecommerce.project.response.RestApiResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final PagableObject pagableObject;

    @Override
    @Cacheable(value = "Categories")
    public RestApiResponse<Page<CategoryDTO>> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage=categoryRepository.findAll(pageable);
        if (categoryPage.getTotalElements()==0)
            throw new RestApiException("No Category found",HttpStatus.NOT_FOUND);
        List<CategoryDTO> categoryDTOList=pagableObject.map(categoryPage.getContent(),CategoryDTO.class);
        Page<CategoryDTO> pagedCategoryDto=new PageImpl<>(categoryDTOList,pageable,categoryPage.getTotalElements());
        return ResponseUtil.getResponse(pagedCategoryDto,"Categories");
        }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByCategoryName(categoryDTO.getCategoryName()) != null)
            throw new RestApiException(String.format("%s already exists",categoryDTO.getCategoryName()), HttpStatus.BAD_REQUEST);
        Category category = pagableObject.map(categoryDTO,Category.class);
        Category savedCategory = categoryRepository.save(category);
        return pagableObject.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()-> new RestApiException(String.format("Category with id %s not found", categoryId),HttpStatus.BAD_REQUEST));

        //deleting the category
        categoryRepository.delete(category);

        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RestApiException(String.format("Category with id %s not found", categoryId),HttpStatus.BAD_REQUEST));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
