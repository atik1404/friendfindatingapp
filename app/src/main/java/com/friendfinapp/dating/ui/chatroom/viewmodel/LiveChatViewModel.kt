package com.friendfinapp.dating.ui.chatroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatMessageDeleteResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatPostingModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class LiveChatViewModel : ViewModel() {

    private val repo = MainRepo()

    fun getChatList(chatPostingModel: LiveChatPostingModel) : LiveData<LiveChatResponseModel> = repo.getChatList(chatPostingModel)

    fun getChatListSearchResult(chatPostingModel: LiveChatPostingModel) : LiveData<LiveChatResponseModel> = repo.getChatList(chatPostingModel)


    fun deleteChatList(
        stringsFromLoop: MutableList<String>,
        customDialog: ProgressCustomDialog?,
        chatRoomActivity: ChatRoomActivity
    ) : LiveData<LiveChatMessageDeleteResponseModel> = repo.getMessageDelete(stringsFromLoop,customDialog,chatRoomActivity)


}