package com.ecommerce.project.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
 private static String encryptionKeyGenerator(){
     SecureRandom random = new SecureRandom();
     byte[] keyBytes = new byte[32]; // 256-bit
     random.nextBytes(keyBytes);
     return Base64.getEncoder().encodeToString(keyBytes);
 }

 private static String encryptionSaltGenerator(){
     SecureRandom random = new SecureRandom();
     byte[] saltBytes = new byte[16]; // 128-bit
     random.nextBytes(saltBytes);
     return Base64.getEncoder().encodeToString(saltBytes);
 }

    public static void main(String[] args) {
        System.out.println("ENCRYPTION_KEY="+encryptionKeyGenerator());
        System.out.println("ENCRYPTION_SALT="+encryptionSaltGenerator());
    }

}
