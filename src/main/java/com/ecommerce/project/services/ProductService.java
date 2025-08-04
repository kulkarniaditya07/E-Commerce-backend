package com.ecommerce.project.services;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.response.RestApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

   // ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    ProductDTO addProductToCategory(ProductDTO productDTO, Long categoryId);


    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);


    ProductDTO uploadImageToServer(Long productId, MultipartFile file);


    RestApiResponse<Page<ProductDTO>> findAllProducts(Pageable pageable);
}
