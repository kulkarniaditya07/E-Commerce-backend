package com.ecommerce.project.common;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int LENGTH = 8;

    public static String generatePassword(){
        Random random=new Random();

        return IntStream.generate(()-> random.nextInt(CHARACTERS.length()))
                .limit(LENGTH)
                .mapToObj(i-> String.valueOf(CHARACTERS.charAt(i)))
                .collect(Collectors.joining());
    }

//    public static void main(String[] args) {
//        System.out.println(generatePassword());
//    }

}
