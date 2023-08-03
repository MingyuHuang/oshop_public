package com.melon.dto;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UserDetailsDTO implements UserDetails{

    private String username;
    private String password;
    @JsonIgnore
    private Set<SimpleGrantedAuthority> authorities;
    private Set<String> authoritiesString;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
