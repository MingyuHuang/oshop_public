package com.melon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.melon.dto.ExceptionDTO;
import com.melon.dto.UserDetailsDTO;
import com.melon.exception.ExceptionMessage;
import com.melon.feign.UserFeignService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.ByteBufferDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserFeignService userFeignService;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final String JWT_EXCEPTION = "JWTException";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt = header.split(" ")[1];

        if (jwt == null || "null".equalsIgnoreCase(jwt)){
            ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED.value())
                    .message(ExceptionMessage.EMPTY_JWT.getMessage())
                    .build();
            request.setAttribute(JWT_EXCEPTION, exceptionDTO);
            filterChain.doFilter(request, response);
            return;
        }

        UserDetailsDTO userDetails;
        try{

            userDetails = userFeignService.getUserDetails(jwt);
        } catch (FeignException exception){

            ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                    .httpStatus(4011)
                    .message(ExceptionMessage.EXPIRED_JWT.getMessage())
                    .build();

            request.setAttribute(JWT_EXCEPTION, exceptionDTO);
            filterChain.doFilter(request, response);
            return;
        }

        userDetails.setAuthorities(userDetails.getAuthoritiesString().stream()
                .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toSet()));
        if (userDetails != null){
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
