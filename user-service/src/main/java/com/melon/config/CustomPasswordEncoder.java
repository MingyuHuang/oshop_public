package com.melon.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("customPasswordEncoder")
public class CustomPasswordEncoder extends BCryptPasswordEncoder {
}
