package com.friendfinapp.dating.ui.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class ChatSearchViewModel : ViewModel() {


    private val repo = MainRepo()

    val suggestionItem= repo.suggetionData
    val searchResult = repo.searchResult
    fun getServerChatDataHintByKeyword(userName: String, newText: String) = repo.getServerChatDataHintByKeyword(userName,newText)
    fun getSearchResult(myUserName: String?, otherUserName: String) =repo.getSearchResult(myUserName,otherUserName)

}