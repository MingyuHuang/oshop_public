package com.melon.config;

import com.melon.dto.UserDetailsDTO;
import com.melon.entity.CurrentUser;
import com.melon.feign.UserFeignService;
import org.checkerframework.checker.units.qual.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("customUserDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeignService userFeignService;
    @Override
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {

        UserDetailsDTO userDetailsDTO = userFeignService.getUserDetails(jwt);
        CurrentUser currentUser = new CurrentUser(userDetailsDTO);
        return currentUser;
    }
}
