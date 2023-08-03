package com.melon.entity;

public enum Oauth2Provider {

    GITHUB("github"),

    GOOGLE("google"),
    LOCAL("local"),
    ;
    private String provider;

    public String getProvider() {
        return provider;
    }

    Oauth2Provider(final String provider) {
        this.provider = provider;
    }
}
