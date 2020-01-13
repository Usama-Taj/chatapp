package com.example.mychatapp.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class User {
    public String uid;
    public String username;
    public String email;
    public String firebaseToken;
    public String password;
    public String gender;
    public String userimage;

    public User(){

    }

    public User(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public User(String uid, String username, String email, String firebaseToken, String password, String gender) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.password = password;
        this.gender = gender;
    }

    public User(String username, String userimage) {
        this.username = username;
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

}

