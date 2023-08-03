package com.melon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.service.impl.TokenService;
import com.melon.dto.AuthenticationResponse;
import com.melon.dto.CurrentUser;
import com.melon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper mapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null){
            context.setAuthentication(authentication);
        }
        String token = tokenService.generateToken(currentUser);
        tokenService.saveCurrentToken(currentUser.getAppUser().getId().toString(), token);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .build();
        response.getWriter().write(mapper.writeValueAsString(authenticationResponse));
    }
}
