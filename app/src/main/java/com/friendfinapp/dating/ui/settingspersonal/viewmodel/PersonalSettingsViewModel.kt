package com.friendfinapp.dating.ui.settingspersonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.model.LogOutResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.settingspersonal.PersonalSettingsActivity
import com.friendfinapp.dating.ui.settingspersonal.model.PasswordChangeResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsGetDataResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsPostingModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.UpdatePersonalSettingsResponseModel

class PersonalSettingsViewModel : ViewModel() {

    private val repo = MainRepo()

    fun deleteUser(
        model: PersonalSettingsPostingModel,
        customDialog: ProgressCustomDialog?,
        personalSettingsActivity: PersonalSettingsActivity
    ) : LiveData<PersonalSettingsResponseModel> = repo.delete(model,customDialog,personalSettingsActivity)

    fun getLogOut() : LiveData<LogOutResponseModel> = repo.getLogoutUser()

    fun personalSettingsGetData(username: String?) : LiveData<PersonalSettingsGetDataResponseModel> = repo.personalSettingsGetData(username)

    fun personalSettingsChangePassword(username: String?,oldPassword: String?,newPassword: String?) : LiveData<PasswordChangeResponseModel> = repo.getPasswordChange(username,oldPassword,newPassword!!)
    fun updatePersonalSettings(
        userName: String?,
        country: String,
        state: String,
        city: String,
        zip: String,
        fullName: String,
        interest: Int,
        dateOfBirth: String,
        email: String
    ) : LiveData<UpdatePersonalSettingsResponseModel> = repo.getUpdatePersonalSettings(userName,country,state,city,zip,fullName,interest,dateOfBirth,email)
}