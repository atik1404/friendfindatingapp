package com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPostingModel {

//    @SerializedName("gender")
//    @Expose
//    public int gender;
//    @SerializedName("minVotes")
//    @Expose
//    public int minVotes;
//    @SerializedName("maxTimeAway")
//    @Expose
//    public int maxTimeAway;
//    @SerializedName("maxResults")
//    @Expose
//    public int maxResults;
//
//    public SearchPostingModel(int gender, int minVotes, int maxTimeAway, int maxResults) {
//        this.gender = gender;
//        this.minVotes = minVotes;
//        this.maxTimeAway = maxTimeAway;
//        this.maxResults = maxResults;
//    }




    @SerializedName("gender")
    @Expose
    public int gender;
    @SerializedName("maxTimeAway")
    @Expose
    public int maxTimeAway;
    @SerializedName("pageNo")
    @Expose
    public int pageNo;
    @SerializedName("itemPerPage")
    @Expose
    public int itemPerPage;

    public SearchPostingModel(int gender, int maxTimeAway, int pageNo, int itemPerPage) {
        this.gender = gender;
        this.maxTimeAway = maxTimeAway;
        this.pageNo = pageNo;
        this.itemPerPage = itemPerPage;
    }
}
