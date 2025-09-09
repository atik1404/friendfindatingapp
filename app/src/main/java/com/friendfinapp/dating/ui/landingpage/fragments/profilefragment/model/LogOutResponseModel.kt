package com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LogOutResponseModel {

    @SerializedName("status_code")
    @Expose
    var status_code = 0
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: Any? = null
    @SerializedName("count")
    @Expose
    var count = 0
}