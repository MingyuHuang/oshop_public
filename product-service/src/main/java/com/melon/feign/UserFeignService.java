package com.melon.feign;

import com.melon.dto.UserDetailsDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface UserFeignService {

    @GetMapping("/authentication/{token}")
    @Headers("Authorizaztion:Bearer {token}")
    UserDetailsDTO getUserDetails(@PathVariable("token") String token);
}
