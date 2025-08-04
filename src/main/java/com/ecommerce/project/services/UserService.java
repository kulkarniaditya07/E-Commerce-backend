package com.ecommerce.project.services;

import com.ecommerce.project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService{
    private final UserRepository userRepository;
   public UserDetailsService userDetailsService(){
       return new UserDetailsService() {
           @Override
           public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
              return userRepository.findByEmail(username)
                       .orElseThrow(()->new UsernameNotFoundException("User not found"));

           }
       };
   }

}
