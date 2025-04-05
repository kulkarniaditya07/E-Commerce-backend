package com.ecommerce.project.services.impl;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = "categories",
        key = "T(java.util.Objects).hash(#pageNumber, #pageSize, #sortBy, #sortDir)",
            unless = "#result==null or #result.content.isEmpty()"
        )
    @Transactional
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sortByAndDir= sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize, sortByAndDir);
        Page<Category> categoryPage=categoryRepository.findAll(pageDetails);

        List<Category> savedCategories= categoryPage.getContent();

        if(savedCategories.isEmpty()){
            throw new APIException("No category created yet!!!");
        }

        List<CategoryDTO> categoryDTO = savedCategories.stream()
                .map(category-> modelMapper.map(category, CategoryDTO.class))
                .toList();

        return CategoryResponse.builder()
                .content(categoryDTO)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalpages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();
        }

    @Override

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null)
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!!");
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        //deleting the category
        categoryRepository.delete(category);

        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
