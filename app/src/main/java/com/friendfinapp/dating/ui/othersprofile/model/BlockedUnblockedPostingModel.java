package com.friendfinapp.dating.ui.othersprofile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockedUnblockedPostingModel {

    @SerializedName("userBlocker")
    @Expose
    private String userBlocker;
    @SerializedName("blockedUser")
    @Expose
    private String blockedUser;

    public BlockedUnblockedPostingModel(String userBlocker, String blockedUser) {
        this.userBlocker = userBlocker;
        this.blockedUser = blockedUser;
    }
}
