package com.friendfinapp.dating.ui.uploadphoto.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UploadPhotoResponseModel {

    @SerializedName("status_code")
    @Expose
     val statusCode: Int? = null


    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     val data: Any? = null

    @SerializedName("count")
    @Expose
     val count: Int? = null

}