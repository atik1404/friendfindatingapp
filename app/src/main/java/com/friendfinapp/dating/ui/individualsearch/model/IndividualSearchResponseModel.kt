package com.friendfinapp.dating.ui.individualsearch.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class IndividualSearchResponseModel {

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


    class Data{
        @SerializedName("gender")
        @Expose
         val gender: Int? = null

        @SerializedName("seeking")
        @Expose
         val seeking: Int? = null

        @SerializedName("age")
        @Expose
         val age: Int? = null

        @SerializedName("country")
        @Expose
         val country: String? = null

        @SerializedName("state")
        @Expose
         val state: String? = null

        @SerializedName("city")
        @Expose
         val city: String? = null

        @SerializedName("username")
        @Expose
         val username: String? = null

        @SerializedName("userimage")
        @Expose
         val userimage: String? = null

        @SerializedName("base64Image")
        @Expose
         val base64Image: Any? = null
    }
}