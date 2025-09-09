package com.friendfinapp.dating.ui.reportanabuse.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportAbusedPostingModel {

    @SerializedName("reportedBy")
    @Expose
    private String reportedBy;
    @SerializedName("reportedUser")
    @Expose
    private String reportedUser;
    @SerializedName("reportNote")
    @Expose
    private String reportNote;


    public ReportAbusedPostingModel(String reportedBy, String reportedUser, String reportNote) {
        this.reportedBy = reportedBy;
        this.reportedUser = reportedUser;
        this.reportNote = reportNote;
    }
}
