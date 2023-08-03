package com.melon.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String password;
    private String provider;
    private String providerUserId;
}
