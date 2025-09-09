package com.friendfinapp.dating.ui.saveprofile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveProfileResponseModel {
    @SerializedName("status_code")
    @Expose
    var status_code = 0
    @SerializedName("statusmessage_code")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: Any? = null
    @SerializedName("count")
    @Expose
    var count = 0
}