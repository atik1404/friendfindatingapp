package com.friendfinapp.dating.ui.settingspersonal.model

import com.friendfinapp.dating.ui.signin.model.LoginResponseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PersonalSettingsGetDataResponseModel {

    @SerializedName("status_code")
    @Expose
    var status_code = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("count")
    @Expose
    var count = 0

    class Data {

        @SerializedName("username")
        @Expose
        var username: String? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("zipCode")
        @Expose
        var zipCode: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("interestedIn")
        @Expose
        var interestedIn = 0

        @SerializedName("birthdate")
        @Expose
        var birthdate: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

    }
}