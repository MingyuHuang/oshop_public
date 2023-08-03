package com.melon.security.oauth2.user;

import com.melon.entity.Oauth2Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String providerId, Map<String, Object> attributes){

        if (providerId.equalsIgnoreCase(Oauth2Provider.GITHUB.getProvider())){

            return new GithubUserInfo(attributes);
        }else{
            throw new RuntimeException("Login in with " + providerId + " has not been supported");
        }
    }
}
