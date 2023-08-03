//package com.melon.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//@EnableAuthorizationServer
//@Configuration
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    @Qualifier("customUserDetailsService")
//    private UserDetailsService userDetailsService;
//    @Autowired
//    @Qualifier("springTokenStore")
//    private TokenStore springTokenStore;
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        clients
//                .inMemory()
//                .withClient("client")
//                .secret(passwordEncoder.encode("client"))
//                .redirectUris("http://127.0.0.1:4200")
//                .scopes("all")
//                .authorizedGrantTypes("authorization_code");
//
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//
//        endpoints.authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService)
//                .tokenStore(springTokenStore)
//                .accessTokenConverter(jwtAccessTokenConverter);
//    }
//}
