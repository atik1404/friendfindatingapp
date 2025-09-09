package com.friendfinapp.dating.ui.chatroom.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveChatPostingModel {

    @SerializedName("fromUsername")
    @Expose
    private String fromUsername;
    @SerializedName("toUsername")
    @Expose
    private String toUsername;

    public LiveChatPostingModel(String fromUsername, String toUsername) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }
}
