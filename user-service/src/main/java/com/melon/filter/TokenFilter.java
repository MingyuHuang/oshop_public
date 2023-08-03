package com.melon.filter;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.dto.CurrentUser;
import com.melon.dto.ExceptionDTO;
import com.melon.exception.ExceptionMessage;
import com.melon.service.impl.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private final TokenService tokenService;
    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;
    ObjectMapper mapper = new ObjectMapper();

    private final String JWT_EXCEPTION = "JWTException";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null){
            ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED.value())
                    .message(ExceptionMessage.NO_AUTHORIZATION_HEADER.getMessage())
                    .build();
            request.setAttribute(JWT_EXCEPTION, exceptionDTO);
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = header.split(" ")[1];
        if(StringUtils.equals(jwt, "null")){
            ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED.value())
                    .message(ExceptionMessage.EMPTY_JWT.getMessage())
                    .build();
            request.setAttribute(JWT_EXCEPTION, exceptionDTO);
            filterChain.doFilter(request, response);
            return;
        }

        String userId;
        try {

            userId= tokenService.extractUserId(jwt);
        } catch (ExpiredJwtException expiredJwtException){

            userId = expiredJwtException.getClaims().getSubject();
            ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED.value())
                    .message(ExceptionMessage.EXPIRED_JWT.getMessage())
                    .build();
            request.setAttribute(JWT_EXCEPTION, exceptionDTO);
            tokenService.removeCurrentToken(userId);
            filterChain.doFilter(request, response);
            return;
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            CurrentUser currentUser = (CurrentUser) userDetailsService.loadUserByUsername(userId);
            if (tokenService.isTokenValid(jwt, currentUser)){

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser.getUsername(), null, currentUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{

                tokenService.removeCurrentToken(userId);
            }
        }
        filterChain.doFilter(request, response);
    }
}
