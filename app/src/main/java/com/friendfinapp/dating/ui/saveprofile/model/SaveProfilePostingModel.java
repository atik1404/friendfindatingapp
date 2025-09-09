package com.friendfinapp.dating.ui.saveprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveProfilePostingModel {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("questionID")
    @Expose
    public int questionID;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("approved")
    @Expose
    public int approved;

    public SaveProfilePostingModel(String username, int questionID, String value, int approved) {
        this.username = username;
        this.questionID = questionID;
        this.value = value;
        this.approved = approved;
    }


}
