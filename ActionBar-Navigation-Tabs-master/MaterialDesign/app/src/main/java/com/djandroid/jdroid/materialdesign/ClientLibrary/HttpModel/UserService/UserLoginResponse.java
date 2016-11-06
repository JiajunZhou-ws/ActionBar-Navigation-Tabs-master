package com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.UserService;

/**
 * Created by Jimmy on 2016/10/24.
 */
public class UserLoginResponse {
    private UserLoginStatus status;
    private UserInformation userInformation;

    public UserLoginStatus getStatus() {
        return status;
    }

    public void setStatus(UserLoginStatus status) {
        this.status = status;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }
}

