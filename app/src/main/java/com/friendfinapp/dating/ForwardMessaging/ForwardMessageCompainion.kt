package com.friendfinapp.dating.ForwardMessaging

import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import java.util.ArrayList

class ForwardMessageCompainion {
    companion object {
        var selectedMessageList: MutableList<LiveChatResponseModel.Data> = ArrayList()
    }
}