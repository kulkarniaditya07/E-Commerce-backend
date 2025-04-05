package com.ecommerce.project.model;

import com.ecommerce.project.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", uniqueConstraints = {
                    @UniqueConstraint(columnNames = "username"),
                    @UniqueConstraint(columnNames = "email")
}
)
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @Email
    private String Email;

    @Column(name = "password")
    @ValidPassword(message = "Password should contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @NotBlank(message = "Password should not be blank")
    private String password;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    Set<Role> roles=new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = {CascadeType.PERSIST,CascadeType.REMOVE, CascadeType.MERGE})
    private Set<Product> products;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(
            name = "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    List<Address> addresses=new ArrayList<>();

    private boolean enabled;

    public User(String email, String firstName, String lastName, String password, String username) {
        Email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
    }


}
