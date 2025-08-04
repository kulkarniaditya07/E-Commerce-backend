package com.ecommerce.project.util;

import com.ecommerce.project.config.ConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final ConfigProperties configProperties;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    
    public String generateToken(UserDetails userDetails){
        return jwtToken(userDetails);
    }

    private String jwtToken(UserDetails userDetails) {
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keys= Decoders.BASE64.decode(configProperties.getJwtSecretKey());
        return Keys.hmacShaKeyFor(keys);
    }

}
