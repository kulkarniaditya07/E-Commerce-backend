package com.ecommerce.project.model;

import com.ecommerce.project.config.AppRoles;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "roles")
    private List<AppRoles> appRoles;

    private String name;


}
