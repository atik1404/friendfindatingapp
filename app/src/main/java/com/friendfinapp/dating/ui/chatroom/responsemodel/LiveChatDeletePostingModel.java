package com.friendfinapp.dating.ui.chatroom.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveChatDeletePostingModel {
    @SerializedName("fromUsername")
    @Expose
    private List<String> id;

    public LiveChatDeletePostingModel(List<String> id) {
        this.id = id;
    }
}
