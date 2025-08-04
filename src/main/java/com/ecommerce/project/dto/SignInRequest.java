package com.ecommerce.project.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequest implements Serializable {

    private String username;
    private String password;

}
