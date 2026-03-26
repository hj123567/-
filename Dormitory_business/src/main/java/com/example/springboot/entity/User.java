package com.example.springboot.entity;

public class User {
    private String username;
    private String password;
    private String identity;
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User() {
    }

    public User(String username, String password, String identity, String avatar) {
        this.username = username;
        this.password = password;
        this.identity = identity;
        this.avatar = avatar;
    }
}
