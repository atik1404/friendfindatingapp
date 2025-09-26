package com.friendfinapp.dating.ui.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.signin.model.LoginResponseModel
import com.friendfinapp.dating.ui.signup.model.RegisterResponseModel

class RegisterViewModel : ViewModel() {
    private val repo = MainRepo()
    fun signUpUser(
        userName: String,
        password: String,
        email: String,
        fullName: String,
        gen: Int,
        active: Int,
        receiveEmails: Int,
        interest: Int,
        dateofbirth: String,
        dateofbirth2: String,
        country: String,
        state: String,
        postalcode: String,
        city: String,
        ip: String,
        messageVerificationsLeft: Int,
        languageId: Int,
        billingDetails: String?,
        invitedBy: String,
        incomingMessagesRestrictions: String?,
        affiliateID: Int,
        options: Int,
        longitude: Int,
        latitude: Int,
        tokenUniqueId: String?,
        credits: Int,
        moderationScore: Int,
        spamSuspected: Int,
        faceControlApproved: Int,
        profileSkin: String,
        statusText: String,
        featuredMember: Int,
        mySpaceID: String,
        facebookID: Int,
        eventsSettings: Int
    ) : LiveData<RegisterResponseModel> = repo.registerUser(userName,password,email,fullName,gen,active,receiveEmails,interest,dateofbirth,dateofbirth2,country,state,
    postalcode,city,ip,messageVerificationsLeft,languageId,billingDetails,invitedBy,incomingMessagesRestrictions,affiliateID,options,longitude,
    latitude,tokenUniqueId,credits,moderationScore,spamSuspected,faceControlApproved,profileSkin,statusText,featuredMember,mySpaceID,facebookID,
    eventsSettings)
}