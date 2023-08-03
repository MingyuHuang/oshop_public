package com.melon.service.impl;

import com.melon.config.AppProperties;
import com.melon.dto.CurrentUser;
import com.melon.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class TokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;


    public String generateToken(CurrentUser currentUser){

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", currentUser.getAppUser().getId());
//        extraClaims.put("name", currentUser.getAppUser().getEmail());
        extraClaims.put("roles", currentUser.getAppUser().getRoles());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(currentUser.getAppUser().getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(){

        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getAuth().getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public boolean isTokenValid(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(appProperties.getAuth().getSecret()).parseClaimsJws(authToken);
//            return true;
//        } catch (Exception ex) {
//            log.error(ex.toString());
//            return false;
//        }
//    }

    public boolean isTokenValid(String token, CurrentUser currentUser){

        return extractUserId(token).equals(currentUser.getAppUser().getId().toString()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){

        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserId(String token) throws ExpiredJwtException{

        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public Optional<String> getCurrentToken(String token) {

        return Optional.of(redisTemplate.opsForValue().get(token));
    }

    public void saveCurrentToken(String userId, String token){

        redisTemplate.opsForValue().set(userId, token);
    }

    public void removeCurrentToken(String userId){

        redisTemplate.delete(userId);
    }
}
