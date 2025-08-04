package com.ecommerce.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Privileges{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "priv_seq")
    @SequenceGenerator(name = "priv_seq", sequenceName = "privileges_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "privilege_name")
    private String name;
}
