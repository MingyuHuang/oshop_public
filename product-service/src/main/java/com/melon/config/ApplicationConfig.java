package com.melon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApplicationConfig {

    @Autowired
    private Environment environment;
    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host"), Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));
        cachingConnectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        cachingConnectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitTemplate());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue productQueue() {

        return new Queue("queue_product", true, false, false);
    }

    @Bean
    public Exchange productExchange() {

        return new DirectExchange("product_exchange", true, false);
    }

    @Bean
    public Binding binding() {

        return BindingBuilder
                .bind(productQueue())
                .to(productExchange())
                .with("productRoutingKey")
                .noargs();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{

        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public Docket docket(){

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("Products").build());
    }

//    @Bean
//    public Decoder feignDecoder(){
//
//        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(cutomObjectMapper());
//        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
//        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
//
//
//        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
//    }
//
//    public ObjectMapper cutomObjectMapper(){
//
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper;
//    }
}
