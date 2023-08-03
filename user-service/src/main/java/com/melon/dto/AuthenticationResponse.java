package com.melon.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String message;
}
