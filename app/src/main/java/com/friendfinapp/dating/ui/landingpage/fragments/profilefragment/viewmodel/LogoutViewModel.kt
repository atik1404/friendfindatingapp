package com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.model.LogOutResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class LogoutViewModel : ViewModel() {


    private val repo = MainRepo()

    fun getLogOut() : LiveData<LogOutResponseModel> = repo.getLogoutUser()


}