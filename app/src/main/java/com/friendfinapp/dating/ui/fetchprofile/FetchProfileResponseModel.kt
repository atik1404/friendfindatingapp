package com.friendfinapp.dating.ui.fetchprofile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FetchProfileResponseModel {
    @SerializedName("status_code")
    @Expose
    var status_code = 0

    @SerializedName("userimage")
    @Expose
    var userimage: MutableList<Userimage>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: MutableList<Data>? = null

    @SerializedName("blockedUser")
    @Expose
     val blockedUser: List<BlockedUser>? = null
    @SerializedName("count")
    @Expose
    var count = 0


     class Userimage {
         @SerializedName("userimage")
         @Expose
        var userimage: String? = null
    }

    class BlockedUser{
        @SerializedName("userBlocker")
        @Expose
         val userBlocker: String? = null

        @SerializedName("blockedUser")
        @Expose
         val blockedUser: String? = null
    }

    class Data {
         @SerializedName("questionID")
         @Expose
        var questionID = 0
         @SerializedName("value")
         @Expose
        var value: String? = null
         @SerializedName("approved")
         @Expose
        var approved = 0
    }
}