package com.ecommerce.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

import java.util.List;


@Entity
@AllArgsConstructor
@Table(name = "categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Category implements Serializable {


    @Serial
    private static final long serialVersionUID = 2671390909021638798L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;


    @NotBlank()
    @Size(min=3, message = "Category name must contain at least 3 characters")
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

}
