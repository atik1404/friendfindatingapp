package com.friendfinapp.dating.ui.forgetpassword.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgetPasswordResponseModel {

    @SerializedName("status_code")
    @Expose
    var status_code = 0
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: String? = null
    @SerializedName("count")
    @Expose
    var count = 0
}