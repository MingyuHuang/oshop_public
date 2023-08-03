package com.melon.security.oauth2.user;

import java.util.Map;

public class GithubUserInfo extends OAuth2UserInfo{

    public GithubUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return attributes.get("login").toString();
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        return email != null ? email.toString(): null;
    }

    @Override
    public String getImageUrl() {
        return attributes.get("avatar_url").toString();
    }
}
