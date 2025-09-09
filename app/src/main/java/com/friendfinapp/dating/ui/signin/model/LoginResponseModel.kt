package com.friendfinapp.dating.ui.signin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LoginResponseModel {
    @SerializedName("status_code")
    @Expose
    var status_code = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data : Data? = null

    @SerializedName("count")
    @Expose
    var count = 0

    class Data {
        @SerializedName("token")
        @Expose
        var token: String? = null
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("username")
        @Expose
        var username: String? = null
        @SerializedName("password")
        @Expose
        var password: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("gender")
        @Expose
        var gender = 0
        @SerializedName("active")
        @Expose
        var active = 0
        @SerializedName("receiveEmails")
        @Expose
        var receiveEmails = 0
        @SerializedName("interestedIn")
        @Expose
        var interestedIn = 0
        @SerializedName("birthdate")
        @Expose
        var birthdate: String? = null
        @SerializedName("birthdate2")
        @Expose
        var birthdate2: String? = null
        @SerializedName("country")
        @Expose
        var country: String? = null
        @SerializedName("state")
        @Expose
        var state: String? = null
        @SerializedName("zipCode")
        @Expose
        var zipCode: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null
        @SerializedName("userIP")
        @Expose
        var userIP: String? = null
        @SerializedName("messageVerificationsLeft")
        @Expose
        var messageVerificationsLeft = 0
        @SerializedName("languageId")
        @Expose
        var languageId = 0
        @SerializedName("billingDetails")
        @Expose
        var billingDetails: Any? = null
        @SerializedName("invitedBy")
        @Expose
        var invitedBy: Any? = null
        @SerializedName("incomingMessagesRestrictions")
        @Expose
        var incomingMessagesRestrictions: Any? = null
        @SerializedName("affiliateID")
        @Expose
        var affiliateID = 0
        @SerializedName("options")
        @Expose
        var options = 0
        @SerializedName("longitude")
        @Expose
        var longitude = 0
        @SerializedName("latitude")
        @Expose
        var latitude = 0
        @SerializedName("tokenUniqueId")
        @Expose
        var tokenUniqueId: String? = null
        @SerializedName("credits")
        @Expose
        var credits = 0
        @SerializedName("moderationScore")
        @Expose
        var moderationScore = 0
        @SerializedName("spamSuspected")
        @Expose
        var spamSuspected = 0
        @SerializedName("faceControlApproved")
        @Expose
        var faceControlApproved = 0
        @SerializedName("profileSkin")
        @Expose
        var profileSkin: String? = null
        @SerializedName("statusText")
        @Expose
        var statusText: Any? = null
        @SerializedName("featuredMember")
        @Expose
        var featuredMember = 0
        @SerializedName("mySpaceID")
        @Expose
        var mySpaceID: Any? = null
        @SerializedName("facebookID")
        @Expose
        var facebookID = 0
        @SerializedName("eventsSettings")
        @Expose
        var eventsSettings = 0
    }
}