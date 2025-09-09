package com.friendfinapp.dating.ui.othersprofile.model

import java.io.Serializable

data class ForwardMessagePostingModel(
    val fromUsername : String,
    val toUsernames : List<String>,
    val ids : List<String>,
): Serializable