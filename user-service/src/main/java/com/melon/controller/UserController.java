package com.melon.controller;

import com.melon.service.impl.TokenService;
import com.melon.dto.AuthenticationResponse;
import com.melon.dto.CurrentUser;
import com.melon.dto.RegisterRequest;
import com.melon.dto.UserDetailsDTO;
import com.melon.entity.AppUser;
import com.melon.entity.Oauth2Provider;
import com.melon.exception.UserAlreadyExistAuthenticationException;
import com.melon.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/{id}")
    public Mono<AppUser> getUser(@PathVariable("id") String id) {

        AppUser user = userService.getUser(id);
        return Mono.justOrEmpty(user);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        request.setProvider(Oauth2Provider.LOCAL.getProvider());
        AuthenticationResponse response = new AuthenticationResponse();
        try {

            AppUser registeredUser = userService.register(request);
            CurrentUser currentUser = new CurrentUser(registeredUser);
            String token = this.tokenService.generateToken(currentUser);
            response.setToken(token);

        } catch (UserAlreadyExistAuthenticationException exception) {
            response.setMessage(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/logout/{token}")
    public ResponseEntity<AuthenticationResponse> logout(@PathVariable String token){

        try{

            userService.logout(token);
            return ResponseEntity.ok().build();
        }catch (RuntimeException exception){
            AuthenticationResponse authenticationResponse = new AuthenticationResponse("", exception.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authenticationResponse);
        }
    }

    @GetMapping("/authentication/{token}")
    public ResponseEntity<UserDetailsDTO> getAuthenticatedUser(@PathVariable("token") String token) throws IOException {

        try{

            String userId = tokenService.extractUserId(token);
            AppUser user = this.getUser(userId).block();
            UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                    .username(userId)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .authoritiesString(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();
            return ResponseEntity.ok(userDetailsDTO);
        }catch (ExpiredJwtException exception){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//    @PostMapping("/authenticate")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request){
//
//        return userService.authenticate(request.getUserName(), request.getCredentials());
//    }
}
