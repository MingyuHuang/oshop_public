package com.melon.config;

import com.melon.entity.AppUser;
import com.melon.entity.Oauth2Provider;
import com.melon.entity.Role;
import com.melon.entity.RoleEnum;
import com.melon.repository.RoleRepository;
import com.melon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataBaseSetup implements ApplicationListener<ContextRefreshedEvent> {

    private boolean isSetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (isSetup){
            return;
        }
        Role adminRole = createRole(RoleEnum.ADMIN);
        Role userRole = createRole(RoleEnum.USER);
        createUser("admin", Set.of(adminRole, userRole));
        isSetup = true;
    }

    private Role createRole(RoleEnum roleEnum) {

        return roleRepository.findByName(roleEnum)
                .orElse(roleRepository.saveAndFlush(new Role(roleEnum)));

    }

    private void createUser(String email, Set<Role> roles) {
        AppUser appUser = userRepository.findByEmail(email).orElse(null);
        if (appUser == null){
            appUser = AppUser.builder()
                    .roles(roles)
                    .appUserName("Admin")
                    .email(email)
                    .password(passwordEncoder.encode("admin"))
                    .provider(Oauth2Provider.LOCAL.getProvider())
                    .build();
            userRepository.saveAndFlush(appUser);
        }
    }
}
