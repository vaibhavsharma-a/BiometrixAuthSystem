package com.sangh.bioauth;
public class TokenRequest {
    private String username;
    private String fcmToken;

    public TokenRequest(String username, String fcmToken) {
        this.username = username;
        this.fcmToken = fcmToken;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
