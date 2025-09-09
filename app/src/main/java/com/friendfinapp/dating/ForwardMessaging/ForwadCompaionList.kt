package com.friendfinapp.dating.ForwardMessaging

import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel

class ForwadCompaionList {
    companion object{
       public val userForwardList: MutableList<ChatResponseModel.Data> = mutableListOf()
        val messageList: MutableList<String> = mutableListOf()
    }
}