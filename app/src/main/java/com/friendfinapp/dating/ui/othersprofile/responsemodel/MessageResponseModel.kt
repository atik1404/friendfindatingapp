package com.friendfinapp.dating.ui.othersprofile.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageResponseModel {
    @SerializedName("status_code")
    @Expose
    var status_code = 0
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data = 0
    @SerializedName("count")
    @Expose
    var count = 0
}