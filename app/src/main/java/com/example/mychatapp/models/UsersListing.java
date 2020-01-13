package com.example.mychatapp.models;

public class UsersListing {
    public String userimage;
    public String username;
    public String useremail;

    public UsersListing(String userimage, String username, String useremail) {
        this.userimage = userimage;
        this.username = username;
        this.useremail = useremail;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}
