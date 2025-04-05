package com.ecommerce.project.payload;

import com.ecommerce.project.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO implements Serializable {
    private Long productId;
    private String productName;
    private String description;
    private String image;
    private double price;
    private Integer quantity;
    private double specialPrice;
    private double discount;
    private Category category;

}
