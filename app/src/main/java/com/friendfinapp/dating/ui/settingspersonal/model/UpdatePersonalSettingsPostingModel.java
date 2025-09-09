package com.friendfinapp.dating.ui.settingspersonal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePersonalSettingsPostingModel {
    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("state")
    @Expose
    public String state;


    @SerializedName("city")
    @Expose
    public String city;


    @SerializedName("zipCode")
    @Expose
    public String zipCode;


    @SerializedName("name")
    @Expose
    public String name;


    @SerializedName("interestedIn")
    @Expose
    public int interestedIn;


    @SerializedName("birthdate")
    @Expose
    public String birthdate;


    @SerializedName("email")
    @Expose
    public String email;

    public UpdatePersonalSettingsPostingModel(String username, String country, String state, String city, String zipCode, String name, int interestedIn, String birthdate, String email) {
        this.username = username;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.name = name;
        this.interestedIn = interestedIn;
        this.birthdate = birthdate;
        this.email = email;
    }
}
