package com.friendfinapp.dating.ui.landingpage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationPostingModel {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("token")
    @Expose
    public String token;

    public NotificationPostingModel(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
