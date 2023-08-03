package com.melon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.filter.TokenFilter;
import com.melon.dto.ExceptionDTO;
import com.melon.repository.InMemoryRequestRepository;
import com.melon.security.LoginFailureHandler;
import com.melon.security.LoginSuccessHandler;
import com.melon.security.oauth2.CustomOauth2UserService;
import com.melon.security.oauth2.Oauth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectMapper mapper;
    private final TokenFilter tokenFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    @Autowired
    private CustomOauth2UserService customOauth2UserService;
    private final String JWT_EXCEPTION = "JWTException";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/oauth2/**", "/login/**", "/users/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .successHandler(this.loginSuccessHandler)
                .failureHandler(this.loginFailureHandler)
                .and()
                .oauth2Login()
                .authorizationEndpoint().authorizationRequestRepository(new InMemoryRequestRepository())
                .and()
                .userInfoEndpoint()
                .userService(customOauth2UserService)
                .and()
                .successHandler(this.oauth2LoginSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this::authenticationEntryPoint);
        // authenticationProvider is configured in ApplicationConfig.class, it includes UserDetails which enables custom Username login
        // and BCryptPasswordEncoder which encrypts the password
        http.authenticationProvider(this.authenticationProvider);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8082"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

//    private void successHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//
//        String token = tokenStore.generateToken(authentication);
////        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("accessToken", token)));
//    }

    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ExceptionDTO exceptionDTO = (ExceptionDTO) request.getAttribute(JWT_EXCEPTION);
        System.out.println(exceptionDTO.getMessage());
        response.getWriter().write(mapper.writeValueAsString(exceptionDTO));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
