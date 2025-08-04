package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.services.ProductService;
import com.ecommerce.project.services.impl.ProductServiceImpl;
import com.ecommerce.project.response.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "Product API", description = "API for product-related operations")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }


    @GetMapping("/public/category/product")
    @Operation(
            summary = "Get all products"
    )
    public ResponseEntity<RestApiResponse<Page<ProductDTO>>> getAllProducts(@PageableDefault(value = 5)
                                                          @SortDefault.SortDefaults({
                                                                  @SortDefault(value = "productId",direction = Sort.Direction.ASC)})Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProducts(pageable));
    }

    @PostMapping("/admin/category/{categoryId}/product")
    @Operation(
            summary = "Adds product to DB"
    )
    public ResponseEntity<ProductDTO> addProductToCategory(@Valid @RequestBody ProductDTO productDTO,
                                                           @PathVariable Long categoryId){
        ProductDTO product = productService.addProductToCategory(productDTO,categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    @Operation(
            summary = "Gets product by Category."
    )
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    @Operation(
            summary = "Gets product by keyword."
    )
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/category/product/{productId}")
    @Operation(
            summary = "Updates a product"
    )
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO product=productService.updateProduct(productDTO,productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @DeleteMapping("/admin/category/product/{productId}")
    @Operation(
            summary = "Delete a product"
    )
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO=productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }


    @PutMapping(value = "/product/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Upload a product image",
            description = "Uploads an image file for a specific product."
    )
    public ResponseEntity<ProductDTO> uploadProductImage(
            @PathVariable Long productId,
            @Parameter(
                    description = "Image file to be uploaded",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("image") MultipartFile file) {

        ProductDTO productDTO = productService.uploadImageToServer(productId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);

    }

}
