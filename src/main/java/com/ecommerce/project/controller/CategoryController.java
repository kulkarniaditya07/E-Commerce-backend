package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Category API", description = "API for category-related operations")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/public/categories")
    @Operation(
            summary = "Gets all categories"
    )
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                             @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        // Return the current list of categories
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @PostMapping("/public/categories")
    @Operation(
            summary = "Create a new category"
    )
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDto) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(
            summary = "Deletes the category"
    )
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO CategoryDTO= categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(CategoryDTO);
    }

    @PutMapping("/public/categories/{categoryId}")
    @Operation(
            summary = "Updates the category"
    )
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO=categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(savedCategoryDTO);

    }


}
