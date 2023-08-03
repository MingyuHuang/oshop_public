package com.melon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.dto.ExceptionDTO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenFilter tokenFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String JWT_EXCEPTION = "JWTException";

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors()
                .and()
                .formLogin().disable()
                .authorizeRequests().antMatchers("/login/**").permitAll()
//                .antMatchers("/products/**").anonymous()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(this::accessDeniedHandler)
                .authenticationEntryPoint(this::authenticationEntryPoint);
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ExceptionDTO exceptionDTO = (ExceptionDTO) request.getAttribute(JWT_EXCEPTION);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(exceptionDTO));
    }


    public void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException{

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(Collections.singletonMap("error", "No Access to this resource")));
        writer.flush();
        writer.close();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://127.0.0.1:8082"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
