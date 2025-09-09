package com.friendfinapp.dating.ui.signup.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RegisterResponseModel {
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
        var gender: Int? = null

        @SerializedName("active")
        @Expose
        var active: Int? = null

        @SerializedName("receiveEmails")
        @Expose
        var receiveEmails: Int? = null

        @SerializedName("interestedIn")
        @Expose
        var interestedIn: Int? = null

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
        var messageVerificationsLeft: Int? = null

        @SerializedName("languageId")
        @Expose
        var languageId: Int? = null

        @SerializedName("billingDetails")
        @Expose
        var billingDetails: Any? = null

        @SerializedName("invitedBy")
        @Expose
        var invitedBy: String? = null

        @SerializedName("incomingMessagesRestrictions")
        @Expose
        var incomingMessagesRestrictions: Any? = null

        @SerializedName("affiliateID")
        @Expose
        var affiliateID: Int? = null

        @SerializedName("options")
        @Expose
        var options: Int? = null

        @SerializedName("longitude")
        @Expose
        var longitude: Int? = null

        @SerializedName("latitude")
        @Expose
        var latitude: Int? = null

        @SerializedName("tokenUniqueId")
        @Expose
        var tokenUniqueId: String? = null

        @SerializedName("credits")
        @Expose
        var credits: Int? = null

        @SerializedName("moderationScore")
        @Expose
        var moderationScore: Int? = null

        @SerializedName("spamSuspected")
        @Expose
        var spamSuspected: Int? = null

        @SerializedName("faceControlApproved")
        @Expose
        var faceControlApproved: Int? = null

        @SerializedName("profileSkin")
        @Expose
        var profileSkin: String? = null

        @SerializedName("statusText")
        @Expose
        var statusText: String? = null

        @SerializedName("featuredMember")
        @Expose
        var featuredMember: Int? = null

        @SerializedName("mySpaceID")
        @Expose
        var mySpaceID: String? = null

        @SerializedName("facebookID")
        @Expose
        var facebookID: Int? = null

        @SerializedName("eventsSettings")
        @Expose
        var eventsSettings: Int? = null

    }
}