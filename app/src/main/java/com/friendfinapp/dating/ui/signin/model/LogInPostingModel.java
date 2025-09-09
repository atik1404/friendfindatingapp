package com.friendfinapp.dating.ui.signin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInPostingModel {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;


    public LogInPostingModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
