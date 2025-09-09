package com.friendfinapp.dating.ui.individualsearch.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class IndividualSearchNewResponseModel {
    @SerializedName("status_code")
    @Expose
    private val statusCode: Int? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Data>? = null

    @SerializedName("count")
    @Expose
    val count: Int? = null

    class Data {
        @SerializedName("username")
        @Expose
        val username: String? = null

        @SerializedName("userimage")
        @Expose
        val userimage: String? = null
    }

}