package ru.praktikum.data;

public class UserCreds {
    private String accessToken;
    private String refreshToken;
    private UserInfo user;

    public String getAccessToken() {
        return accessToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}