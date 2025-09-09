package com.friendfinapp.dating.ui.forgetpassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.ui.forgetpassword.ForgetPasswordActivity
import com.friendfinapp.dating.ui.forgetpassword.model.ForgetPasswordResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class ForgetPasswordViewModel : ViewModel() {


    private val repo = MainRepo()

    fun forgetPasswordChange(
        email: String,
        customDialog: ProgressCustomDialog,
        forgetPasswordActivity: ForgetPasswordActivity
    ) : LiveData<ForgetPasswordResponseModel> =repo.forgetPasswordChange(email, customDialog,forgetPasswordActivity)
}