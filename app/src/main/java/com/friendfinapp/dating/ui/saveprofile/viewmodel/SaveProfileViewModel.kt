package com.friendfinapp.dating.ui.saveprofile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfilePostingModel
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfileResponseModel

class SaveProfileViewModel :ViewModel() {

    private val repo = MainRepo()

    fun saveProfile(saveProfileList: MutableList<SaveProfilePostingModel>) : LiveData<SaveProfileResponseModel> = repo.saveProfile(saveProfileList)


}