package com.friendfinapp.dating.ui.individualsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndividualSearchPostingModel {

    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("seeking")
    @Expose
    private Object seeking;
    @SerializedName("age_Min")
    @Expose
    private Integer ageMin;
    @SerializedName("age_Max")
    @Expose
    private Integer ageMax;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("username")
    @Expose
    private String username;

    public IndividualSearchPostingModel(Object gender, Object seeking, Integer ageMin, Integer ageMax, String country, String state, String city, String username) {
        this.gender = gender;
        this.seeking = seeking;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.country = country;
        this.state = state;
        this.city = city;
        this.username = username;
    }
}
