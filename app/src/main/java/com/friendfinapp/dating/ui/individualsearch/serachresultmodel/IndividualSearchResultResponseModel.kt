package com.friendfinapp.dating.ui.individualsearch.serachresultmodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class IndividualSearchResultResponseModel {

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
    private val count: Int? = null

    class Data {
        @SerializedName("username")
        @Expose
         val username: String? = null

        @SerializedName("userimage")
        @Expose
        val userimage: String? = null
    }
}