package com.friendfinapp.dating.ui.landingpage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.landingpage.model.NotificationResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class NotificationViewModel : ViewModel() {

    private val repo = MainRepo()
    fun notificationTokenUpdate(token: String, userName: String) : LiveData<NotificationResponseModel> = repo.notificationTokenUpdate(token,userName)

}