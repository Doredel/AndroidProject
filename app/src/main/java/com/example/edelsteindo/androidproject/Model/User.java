package com.example.edelsteindo.androidproject.Model;

import android.media.Image;

/**
 * Created by edelsteindo on 26/06/2017.
 */

public class User {

    private String profilePicUrl;
    private String fullName;
    private String userName;
    private String password;

    public User(String fullName, String userName, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
    }

    public User(String profilePic, String fullName, String userName, String password) {
        this.profilePicUrl = profilePic;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
    }

    public String getProfilePic() {
        return this.profilePicUrl;
    }

    public void setProfilePic(String profilePic) {
        this.profilePicUrl = profilePic;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
