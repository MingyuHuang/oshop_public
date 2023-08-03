package com.melon.config;

import com.melon.dto.CurrentUser;
import com.melon.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableRedisRepositories
public class ApplicationConfig {

    @Autowired
    private Environment environment;
    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("customPasswordEncoder")
    private PasswordEncoder passwordEncoder;

//    @Value("${spring.rabbitmq.host}")
//    private String host;
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//    @Value("${spring.rabbitmq.username}")
//    private String userName;
//    @Value("${spring.rabbitmq.password}")
//    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){

        return new LettuceConnectionFactory(environment.getProperty("spring.redis.host"), Integer.parseInt(environment.getProperty("spring.redis.port")));
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

//    @Bean
//    public ConnectionFactory connectionFactory(){
//
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host"), Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));
//        cachingConnectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
//        cachingConnectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public MessageConverter messageConverter(){
//
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin(){
//        return new RabbitAdmin(rabbitTemplate());
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(){
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{

        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

}
