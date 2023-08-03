package com.melon.entity;

import com.melon.dto.UserDetailsDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CurrentUser implements UserDetails {

    private UserDetailsDTO userDetailsDTO;

    public CurrentUser(UserDetailsDTO userDetailsDTO) {
        this.userDetailsDTO = userDetailsDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetailsDTO.getAuthoritiesString().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return userDetailsDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetailsDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDetailsDTO.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDetailsDTO.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDetailsDTO.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userDetailsDTO.isEnabled();
    }
}
