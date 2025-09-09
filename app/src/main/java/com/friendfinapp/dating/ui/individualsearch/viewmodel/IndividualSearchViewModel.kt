package com.friendfinapp.dating.ui.individualsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchNewResponseModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class IndividualSearchViewModel : ViewModel() {

    private val repo = MainRepo()
    fun getIndividualSearch(
        gender: String,
        seeking: String,
        minAge: String,
        MaxAge: String,
        country: String,
        state: String,
        city: String,
        userName: String
    ) : LiveData<IndividualSearchResponseModel> = repo.getIndividualSearch(gender,seeking, minAge,MaxAge,country,state,city,userName)





    fun getNewIndividualSearch(userName: String): LiveData<IndividualSearchNewResponseModel> = repo.getNewIndividualSearch(userName)
}