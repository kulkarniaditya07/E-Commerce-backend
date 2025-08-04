package com.ecommerce.project.services.impl;

import com.ecommerce.project.exceptions.RestApiException;
import com.ecommerce.project.entity.Category;
import com.ecommerce.project.entity.Product;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.services.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final PagableObject pagableObject;

    @Override
    @Transactional
    @Cacheable(value = "products")
    public RestApiResponse<Page<ProductDTO>> findAllProducts(Pageable pageable) {
        Page<Product> productPage= productRepository.findAll(pageable);
        List<ProductDTO> productDTOList= pagableObject.map(productPage.getContent(),ProductDTO.class);
        Page<ProductDTO> pagedProductDto= new PageImpl<>(productDTOList,pageable,productPage.getTotalPages());
        return ResponseUtil.getResponse(pagedProductDto,"Products");
    }
 
    @Override
    @Transactional
    public ProductDTO addProductToCategory(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RestApiException(String.format("Category with id %s not found",categoryId), HttpStatus.NOT_FOUND));

        boolean isProductPresent=true;
        List<Product> products=category.getProducts();
        for(Product product : products){
            isProductPresent=false;
            break;
        }
        if(isProductPresent) {
            productDTO.setCategory(category);

            double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
            productDTO.setSpecialPrice(specialPrice);
            //productDTO.setImages(0);

            Product savedProduct = modelMapper.map(productDTO, Product.class);


            productRepository.save(savedProduct);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new RestApiException("Product Already exists!!",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    @Cacheable(value = "productByKeyword")
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.isEmpty()){
            throw new RestApiException(String.format("Product with name %s not found",keyword),HttpStatus.NOT_FOUND);
        }

        return ProductResponse.builder()
                .content(productDTO)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        // Fetch the existing product with category
        Product savedProductInDB = productRepository.findById(productId)
                .orElseThrow(() -> new RestApiException(String.format("Product with id %s not found",productId),HttpStatus.NOT_FOUND));

        // Preserve the category from the existing product
        Category existingCategory = savedProductInDB.getCategory();

        // Set the product ID in the DTO
        productDTO.setProductId(productId);

        byte[] savedImage=savedProductInDB.getImages();

        // Calculate the special price
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        productDTO.setSpecialPrice(specialPrice);

        // Map the DTO to the Product entity
        modelMapper.map(productDTO,savedProductInDB);

        // Set the preserved category back to the updated product
        savedProductInDB.setCategory(existingCategory);

        //saving existing Image
        savedProductInDB.setImages(savedImage);

        // Save the updated product
        Product updatedProduct=productRepository.save(savedProductInDB);

        // Map the saved product back to DTO and return
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new RestApiException(String.format("Product not found with id %s", productId),HttpStatus.BAD_REQUEST));
        System.out.println(product);

        productRepository.delete(product);

        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    @Transactional
    public ProductDTO uploadImageToServer(Long productId, MultipartFile file) {
        Product getProductFromDB=productRepository.findById(productId)
                .orElseThrow(()-> new RestApiException(String.format("Product with id %s not found",productId),HttpStatus.BAD_REQUEST));

        Product updatedProduct;
        try{
            getProductFromDB.setImageName(file.getOriginalFilename());
            getProductFromDB.setImageType(file.getContentType());
            getProductFromDB.setImages(file.getBytes());
            updatedProduct=productRepository.save(getProductFromDB);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image",e);
        }

        return modelMapper.map(updatedProduct,ProductDTO.class);

    }




    @Override
    @Transactional
    @Cacheable(value = "productByCategory")
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RestApiException(String.format("Category with id %s not found", categoryId),HttpStatus.BAD_REQUEST));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage  = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = productPage.getContent();

        if(products.isEmpty()){
            throw new RestApiException(String.format("%s does not have any products",category.getCategoryName()),HttpStatus.BAD_REQUEST);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ProductResponse.builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }



}
