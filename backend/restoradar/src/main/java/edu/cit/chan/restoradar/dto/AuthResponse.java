package edu.cit.chan.restoradar.dto;

import edu.cit.chan.restoradar.entity.UserEntity;

public class AuthResponse {
    private UserEntity user;
    private String accessToken;
    private String refreshToken;

    public AuthResponse() {}

    public AuthResponse(UserEntity user, String accessToken, String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}