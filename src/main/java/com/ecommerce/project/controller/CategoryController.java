package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.services.CategoryService;
import com.ecommerce.project.response.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Category API", description = "API for category-related operations")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/public/categories")
    @Operation(
            summary = "Gets all categories"
    )
    public ResponseEntity<RestApiResponse<Page<CategoryDTO>>> getAllCategories(@PageableDefault(size = 5)
                                                                 @SortDefault.SortDefaults({@SortDefault(value = "categoryId")})Pageable pageable)
    {
        // Return the current list of categories

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories(pageable));
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
