package com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class ChatViewModel : ViewModel(){


    private val repo = MainRepo()

    fun getChatUser(username: String?, page: Int): LiveData<ChatResponseModel> = repo.getChatUser(username,page)


}