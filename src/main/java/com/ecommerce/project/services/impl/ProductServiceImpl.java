package com.ecommerce.project.services.impl;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    @Cacheable(value = "products")
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sortByAndDir = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndDir);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> savedProducts = productPage.getContent();

        if (savedProducts.isEmpty()) {
            throw new APIException("No Product available!!");
        }

        List<ProductDTO> productDTOList = savedProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
            return ProductResponse.builder()
                    .content(productDTOList)
                    .pageNumber(productPage.getNumber())
                    .pageSize(productPage.getSize())
                    .totalElements(productPage.getTotalElements())
                    .totalPages(productPage.getTotalPages())
                    .lastPage(productPage.isLast())
                    .build();

    }

    @Override
    @Transactional
    public ProductDTO addProductToCategory(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

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
            productDTO.setImage("default.png");

            Product savedProduct = modelMapper.map(productDTO, Product.class);


            productRepository.save(savedProduct);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new APIException("Product Already exists!!");
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
            throw new APIException("Products not found with keyword: " + keyword);
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
                .orElseThrow(() -> new APIException("Product with Product Id: " + productId + ", was not found"));

        // Preserve the category from the existing product
        Category existingCategory = savedProductInDB.getCategory();

        // Set the product ID in the DTO
        productDTO.setProductId(productId);

        String savedImage=savedProductInDB.getImage();

        // Calculate the special price
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        productDTO.setSpecialPrice(specialPrice);

        // Map the DTO to the Product entity
        modelMapper.map(productDTO,savedProductInDB);

        // Set the preserved category back to the updated product
        savedProductInDB.setCategory(existingCategory);

        //saving exisitng Image
        savedProductInDB.setImage(savedImage);

        // Save the updated product
        Product updatedProduct=productRepository.save(savedProductInDB);

        // Map the saved product back to DTO and return
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","product id", productId));
        System.out.println(product);

        productRepository.delete(product);

        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    @Transactional
    public ProductDTO uploadImageToServer(Long productId, MultipartFile file) {
        Product getProductFromDB=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "product id",productId));

        Product updatedProduct;
        try{
            getProductFromDB.setImageName(file.getOriginalFilename());
            getProductFromDB.setImageType(file.getContentType());
            getProductFromDB.setImageData(file.getBytes());
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
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage  = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = productPage.getContent();

        if(products.isEmpty()){
            throw new APIException(category.getCategoryName() + " category does not have any products");
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
