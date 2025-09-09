package com.friendfinapp.dating.ui.settingspersonal.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PersonalSettingsResponseModel {
    @SerializedName("status_code")
    @Expose
    val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: Int? = null

    @SerializedName("count")
    @Expose
    val count: Int? = null
}