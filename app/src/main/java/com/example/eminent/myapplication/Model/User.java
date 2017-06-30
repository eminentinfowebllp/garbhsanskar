package com.example.eminent.myapplication.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 6/21/2017.
 */

public class User {

    @SerializedName("key")
    private String key;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;


    public User(String user_key)
    {
        this.key = user_key;
    }

    public String getUser_key() {
        return key;
    }

    public void setUser_key(String user_key) {
        this.key = user_key;
    }
}
