package com.friendfinapp.dating.ui.othersprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessagePostingModel {

    @SerializedName("fromUsername")
    @Expose
    public String fromUsername;
    @SerializedName("toUsername")
    @Expose
    public String toUsername;
    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("repliedTo")
    @Expose
    public int repliedTo;
    @SerializedName("pendingApproval")
    @Expose
    public int pendingApproval;

    public SendMessagePostingModel(String fromUsername, String toUsername, String body, int repliedTo, int pendingApproval) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.body = body;
        this.repliedTo = repliedTo;
        this.pendingApproval = pendingApproval;
    }
}
