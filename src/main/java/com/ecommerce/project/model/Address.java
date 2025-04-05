package com.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotNull
    @Size(min = 5, message = "Building name should be at least {min} characters")
    private String building;

    @NotNull
    @Size(min = 5, message = "Street name should be at least {min} characters")
    private String street;

    @NotNull
    @Size(min = 4, message = "City name should be at least {min} characters")
    private String city;

    @NotNull
    @Size(min = 5, message = "State name should be at least {min} characters")
    private String state;

    @NotNull
    @Size(min = 5, message = "Country name should be at least {min} characters")
    private String country;

    @NotNull
    @Size(min = 6, message = "Pin code should be at least {min} characters")
    private String pincode;

    @JsonIgnore
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String building, String city, String country, String pincode, String state, String street) {
        this.building = building;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.state = state;
        this.street = street;
    }
}
