package com.ecommerce.project.util;

import com.ecommerce.project.common.EncryptAndDecrypt;
import com.ecommerce.project.config.ConfigProperties;
import com.ecommerce.project.entity.Privileges;
import com.ecommerce.project.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public String generateRefreshToken(UserDetails userDetails){
        return jwtRefreshToken(userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username= EncryptAndDecrypt.decrypt(extractUsername(token));
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    public String getAuthorization(HttpServletRequest requestH) {
        String authorization = requestH.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            authorization = requestH.getHeader("authorization");
        }
        return authorization;
    }

    public String getAttributeValue(String token, String attribute){
        String jwt=token.substring(7);
        Claims claims=extractAllClaims(jwt);
        return EncryptAndDecrypt.decrypt(claims.get(attribute,String.class));
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }






    private String jwtRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(populateCommonClaims(userDetails))
                .setSubject(EncryptAndDecrypt.encrypt(userDetails.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ configProperties.getJwtRefreshExpirationMs()))
                .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }


    private String jwtToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(populateCommonClaims(userDetails))
                .setSubject(EncryptAndDecrypt.encrypt(userDetails.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ configProperties.getJwtExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private Map<String,Object> populateCommonClaims(UserDetails userDetails) {
        User user= (User) userDetails;
        LocalDate now=LocalDate.now();

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime=now.format(formatter);
        return new HashMap<String, Object>(){{
            put("name", EncryptAndDecrypt.encrypt(user.getFirstName()));
            put("email", EncryptAndDecrypt.encrypt(user.getEmail()));
            put("activeRole", EncryptAndDecrypt.encrypt(user.getRoles().getName()));
            put("UserInfo", getUserInfo(user));
            put("LoginTime", formattedDateTime);
        }};

    }

    private Map<String, Object> getUserInfo(User user){
        return new HashMap<String, Object>(){{
            put("username",user.getFirstName());
            put("userEmail",user.getEmail());
            put("role", user.getRoles().getName());
            put("userPrivilege",user.getRoles().getPrivileges().stream().map(Privileges::getName).collect(Collectors.joining(",")));
        }};
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
