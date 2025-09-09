package com.friendfinapp.dating.ui.chatroom.responsemodel

import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LiveChatMessageDeleteResponseModel {
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