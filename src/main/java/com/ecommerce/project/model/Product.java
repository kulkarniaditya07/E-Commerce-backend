package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min=3, message = "The name of the products should have at least {min} characters")
    private String productName;

    @NotBlank
    @Size(min=5, message = "The product description should have at least {min} characters")
    private String description;
    private double price;
    private Integer quantity;
    private double specialPrice;
    private double discount;
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] imageData;

    private String imageName;

    private String imageType;
}
