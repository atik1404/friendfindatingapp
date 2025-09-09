package com.friendfinapp.dating.ui.signup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostingModel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("active")
    @Expose
    private int active;
    @SerializedName("receiveEmails")
    @Expose
    private int receiveEmails;
    @SerializedName("interestedIn")
    @Expose
    private int interestedIn;

    @SerializedName("birthdate")
    @Expose
    private String birthdate;

    @SerializedName("birthdate2")
    @Expose
    private String birthdate2;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("zipCode")
    @Expose
    private String zipCode;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("userIP")
    @Expose
    private String userIP;

    @SerializedName("messageVerificationsLeft")
    @Expose
    private int messageVerificationsLeft;

    @SerializedName("languageId")
    @Expose
    private int languageId;


    public PostingModel(String username, String password, String email, String name, int gender, int active, int receiveEmails, int interestedIn, String birthdate, String birthdate2, String country, String state, String zipCode, String city, String userIP, int messageVerificationsLeft, int languageId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.active = active;
        this.receiveEmails = receiveEmails;
        this.interestedIn = interestedIn;
        this.birthdate = birthdate;
        this.birthdate2 = birthdate2;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
        this.city = city;
        this.userIP = userIP;
        this.messageVerificationsLeft = messageVerificationsLeft;
        this.languageId = languageId;
    }
}