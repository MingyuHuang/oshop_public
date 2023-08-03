package com.melon.configuration;

import com.alipay.api.*;
import com.melon.entity.Cart;
import com.melon.entity.Item;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@EnableRedisRepositories
@EnableSwagger2
public class AppConfig {

    private final Environment environment;

    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {

        return new LettuceConnectionFactory(environment.getProperty("spring.redis.host"), Integer.parseInt(environment.getProperty("spring.redis.port")));
    }

    @Bean
    public ReactiveRedisTemplate<String, Cart> reactiveRedisTemplate() {

        Jackson2JsonRedisSerializer<Cart> serializer = new Jackson2JsonRedisSerializer<>(Cart.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Cart> builder =
                RedisSerializationContext.newSerializationContext(new Jackson2JsonRedisSerializer<>(Cart.class));
        RedisSerializationContext<String, Cart> context = builder.value(serializer).hashValue(serializer).hashKey(serializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory(), context);
    }

    @Bean
    public ReactiveRedisOperations<String, Item> redisOperations() {

        Jackson2JsonRedisSerializer<Item> serializer = new Jackson2JsonRedisSerializer<>(Item.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Item> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, Item> context =
                builder.value(serializer).hashValue(serializer).hashKey(serializer).build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory(), context);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host"), Integer.parseInt(environment.getProperty("spring.rabbitmq.port")));
        factory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        factory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue productsQueue(){

        return new Queue("queue_products", true, false, false);
    }

    @Bean
    public Queue productIdsQueue(){

        return new Queue("queue_cartId", true, false, false);
    }

    @Bean
    public Exchange directExchange(){
        return new DirectExchange("direct_exchange", true, false);
    }


    @Bean
    public Binding binding2(){
        return BindingBuilder
                .bind(productsQueue())
                .to(directExchange())
                .with("routingKey2")
                .noargs();
    }

    @Bean
    public Binding binding3(){
        return BindingBuilder
                .bind(productIdsQueue())
                .to(directExchange())
                .with("routingKey_cartId")
                .noargs();
    }
    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(environment.getProperty("alipay.gateway-url"));
        alipayConfig.setAppId(environment.getProperty("alipay.app-id"));
        alipayConfig.setPrivateKey(environment.getProperty("alipay.merchant-private-key"));
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        alipayConfig.setAlipayPublicKey(environment.getProperty("alipay.alipay-public-key"));
        return new DefaultAlipayClient(alipayConfig);
    }

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("Alipay").build());
    }
}
