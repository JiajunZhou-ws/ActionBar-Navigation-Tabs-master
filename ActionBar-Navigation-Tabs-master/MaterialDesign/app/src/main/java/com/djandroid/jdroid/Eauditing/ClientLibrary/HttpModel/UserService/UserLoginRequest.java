package com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.UserService;

/**
 * Created by Jimmy on 2016/10/24.
 */
public class UserLoginRequest {
    private String username;
    private String password;
    private int version;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordHash(String password) {
        this.password = password;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
