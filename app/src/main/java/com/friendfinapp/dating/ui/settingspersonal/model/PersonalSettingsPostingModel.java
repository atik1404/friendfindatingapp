package com.friendfinapp.dating.ui.settingspersonal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalSettingsPostingModel {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("deleteReason")
    @Expose
    public String deleteReason;

    public PersonalSettingsPostingModel(String username, String deleteReason) {
        this.username = username;
        this.deleteReason = deleteReason;
    }
}
