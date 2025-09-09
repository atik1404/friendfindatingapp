package com.friendfinapp.dating.ui.individualsearch.serachresultmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndividualSearchResultPostingModel {

    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("fromAge")
    @Expose
    private Integer fromAge;
    @SerializedName("toAge")
    @Expose
    private Integer toAge;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("isPhotoRequired")
    @Expose
    private Boolean isPhotoRequired;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("isOnlineUser")
    @Expose
    private Boolean isOnlineUser;
    @SerializedName("bodyType")
    @Expose
    private String bodyType;
    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;
    @SerializedName("eyes")
    @Expose
    private String eyes;
    @SerializedName("hair")
    @Expose
    private String hair;
    @SerializedName("smoking")
    @Expose
    private String smoking;
    @SerializedName("drinking")
    @Expose
    private String drinking;
    @SerializedName("pageNo")
    @Expose
    private Integer pageNo;

    public IndividualSearchResultPostingModel(Integer gender, Integer fromAge, Integer toAge, String country, String state, String city, Boolean isPhotoRequired, String username, Boolean isOnlineUser, String bodyType, String lookingFor, String eyes, String hair, String smoking, String drinking, Integer pageNo) {
        this.gender = gender;
        this.fromAge = fromAge;
        this.toAge = toAge;
        this.country = country;
        this.state = state;
        this.city = city;
        this.isPhotoRequired = isPhotoRequired;
        this.username = username;
        this.isOnlineUser = isOnlineUser;
        this.bodyType = bodyType;
        this.lookingFor = lookingFor;
        this.eyes = eyes;
        this.hair = hair;
        this.smoking = smoking;
        this.drinking = drinking;
        this.pageNo = pageNo;
    }
}
