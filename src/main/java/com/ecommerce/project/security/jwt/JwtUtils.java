package com.ecommerce.project.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger=  LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecretKey;

    @Value("${spring.app.jwtExpiration}")
    private int jwtExpirationTime;


    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if(bearerToken !=null && bearerToken.startsWith("bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username= userDetails.getUsername();
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+jwtExpirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith( (SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Boolean validateJwtToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid Jwt Token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Jwt Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jwt Token Unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Jwt Claims String is empty: {}", e.getMessage());
        }
        return false;
    }



    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }
}
