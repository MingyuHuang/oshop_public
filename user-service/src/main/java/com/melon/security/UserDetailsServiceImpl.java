package com.melon.security;

import com.melon.dto.CurrentUser;
import com.melon.entity.AppUser;
import com.melon.repository.UserRepository;
import com.melon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        // login with UUID (OAuth2 callback + Registration callback)
        Pattern pattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        AppUser appUser = pattern.matcher(account).matches()?
                userService.findUserById(UUID.fromString(account))
//                login with email
                :userService.findUserByEmail(account);

        return new CurrentUser(appUser);
    }



}
