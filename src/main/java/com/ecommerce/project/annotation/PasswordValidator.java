package com.ecommerce.project.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext Context) {
        if(password==null || password.isBlank()){
            return false;
        }
        if(password.length()>8){
            return false;
        }
        String regex= "/^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@/";
       return password.matches(regex);
    }
}
