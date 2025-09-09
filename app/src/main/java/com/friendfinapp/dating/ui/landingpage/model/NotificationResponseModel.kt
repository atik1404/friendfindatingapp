package com.friendfinapp.dating.ui.landingpage.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NotificationResponseModel {

    @SerializedName("status_code")
    @Expose
    private val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
    private val data: Int? = null

    @SerializedName("count")
    @Expose
    private val count: Int? = null
}