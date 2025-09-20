package com.friendfinapp.dating.ui.chatroom.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveChatSearchModel {

    @SerializedName("fromUsername")
    @Expose
    private String fromUsername;
    @SerializedName("toUsername")
    @Expose
    private String toUsername;

    @SerializedName("searchValue")
    @Expose
    private String searchValue;

    public LiveChatSearchModel(String fromUsername, String toUsername) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }
}
