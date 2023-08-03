package com.melon.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.service.impl.TokenService;
import com.melon.dto.AuthenticationResponse;
import com.melon.dto.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;
    private final ObjectMapper mapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        CurrentUser appUser = (CurrentUser) authentication.getPrincipal();
        String token = tokenService.generateToken(appUser);
        tokenService.saveCurrentToken(appUser.getAppUser().getId().toString(), token);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .build();
        mapper.writeValue(outputStream, authenticationResponse);
        outputStream.flush();
        outputStream.close();
    }
}
