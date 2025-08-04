package com.ecommerce.project.util;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtTokenGenerator {
    public static String generator(){
        try{
            KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);//initialize with 256 bits
            SecretKey secretKey= keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(generator());
    }
}
