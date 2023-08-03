package com.melon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AuthController {

    @GetMapping("/info")
    public ResponseEntity<?> authenticate(@AuthenticationPrincipal OAuth2User principal) {

        String userName = principal.getAttributes().get("login").toString();
        return new ResponseEntity(userName, HttpStatus.OK);
    }

}
