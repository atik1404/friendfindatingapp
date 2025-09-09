package com.friendfinapp.dating.ui.chatroom.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LiveChatTokenResponseModel {

    @SerializedName("status_code")
    @Expose
     val statusCode: Int? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     val data: String? = null

    @SerializedName("count")
    @Expose
     val count: Int? = null
}