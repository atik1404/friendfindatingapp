package com.friendfinapp.dating.ui.chatroom.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class LiveChatResponseModel {

    @SerializedName("status_code")
    @Expose
    val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Data>? = null

    @SerializedName("isBlocked")
    @Expose
    val isBlocked: Boolean? = null

    @SerializedName("count")
    @Expose
    val count: Int? = null


    class Data {

        @SerializedName("id")
        @Expose
        val id: String? = null

        @SerializedName("fromUsername")
        @Expose
        val fromUsername: String? = null

        @SerializedName("body")
        @Expose
        val body: String? = null

        @SerializedName("imageURL")
        @Expose
        val imageURL: String? = null

        @SerializedName("audioURL")
        @Expose
        val audioURL: String? = null

        @SerializedName("audioDuration")
        @Expose
        val audioDuration: String? = null

        @SerializedName("videoURL")
        @Expose
        val videoURL: String? = null

        @SerializedName("videoDuration")
        @Expose
        val videoDuration: String? = null

        @SerializedName("sendTime")
        @Expose
        val sendTime: String? = null

        var effectiveDate: String = ""
    }
}