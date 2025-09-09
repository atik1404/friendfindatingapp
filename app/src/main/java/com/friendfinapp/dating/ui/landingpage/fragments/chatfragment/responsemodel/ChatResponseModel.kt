package com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ChatResponseModel {

    @SerializedName("status_code")
    @Expose
     val statusCode: Int? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     val data: List<Data>? = null

    @SerializedName("count")
    @Expose
     val count: Int? = null

    class  Data{
        @SerializedName("toUsername")
        @Expose
         val toUsername: String? = null

        @SerializedName("notificationToken")
        @Expose
         val notificationToken: String? = null

        @SerializedName("userimage")
        @Expose
         val userimage: String? = null
    }

}