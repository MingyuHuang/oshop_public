package com.melon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.dto.AuthenticationResponse;
import com.melon.dto.CurrentUser;
import com.melon.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        System.out.println(exception.getClass());
        if (exception instanceof BadCredentialsException){

            response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("message", "Incorrect password")));
            return;
        }
        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("others", "Unknown error")));
    }
}
