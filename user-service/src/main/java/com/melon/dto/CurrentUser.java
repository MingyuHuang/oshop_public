package com.melon.dto;


import com.melon.entity.AppUser;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrentUser extends User implements OAuth2User {

    @Transient
    private Map<String, Object> attributes;
    @Transient
    private AppUser appUser;

    public CurrentUser(AppUser appUser) {
        super(appUser.getProviderUserId() == null ? appUser.getEmail() : appUser.getAppUserName(), appUser.getPassword(), appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList()));
        this.appUser = appUser;
    }

    public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
