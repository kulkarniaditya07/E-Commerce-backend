package com.ecommerce.project.security.jwt;

import com.ecommerce.project.security.services.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtService;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt=getJwtToken(request);
        if(jwt!=null && jwtService.validateJwtToken(jwt)){
            String username=jwtService.getUsernameFromJwtToken(jwt);
            UserDetails userDetails=userDetailsService.userDetailsService().loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);

    }
    private String getJwtToken(HttpServletRequest header){
        return jwtService.getJwtFromHeader(header);
    }
}
