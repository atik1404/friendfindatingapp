package com.friendfinapp.dating.ui.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.fetchprofile.FetchProfileResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.signin.model.LoginResponseModel

class LogInViewModel :ViewModel() {


    private val repo = MainRepo()

    fun signInUser(username: String, password: String): LiveData<LoginResponseModel> = repo.loginUser(username,password)


    fun signInGoogleUser(email: String): LiveData<LoginResponseModel> = repo.loginGoogleUser(email)


    fun fetchProfile(username: String?) : LiveData<FetchProfileResponseModel> = repo.fetchProfile(username)


}