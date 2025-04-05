package com.ecommerce.project.payload;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest implements Serializable {

    private String username;

    private String Email;

    private String password;
}
