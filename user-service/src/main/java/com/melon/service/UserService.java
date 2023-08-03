package com.melon.service;

import com.melon.dto.AuthenticationResponse;
import com.melon.dto.CurrentUser;
import com.melon.dto.RegisterRequest;
import com.melon.entity.AppUser;

import java.util.Map;
import java.util.UUID;

public interface UserService {

    AppUser saveUser(AppUser user);
    AppUser getUser(String id);

    AppUser register(RegisterRequest request);
//    AuthenticationResponse authenticate(String userName, String credentials);
    CurrentUser processRegistration(String providerId, Map<String, Object> attributes);
    AppUser findUserByEmail(String email);
    AppUser findUserById(UUID id);
    void logout(String token);
}
