package com.friendfinapp.dating.ui.settingspersonal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChangePostingModel {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;

    @SerializedName("newPassword")
    @Expose
    private String newPassword;

    public PasswordChangePostingModel(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
