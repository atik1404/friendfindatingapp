package com.friendfinapp.dating.ui.individualsearch.viewmodel

import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.individualsearch.IndividualSearchInterface
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultPostingModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class IndividualSearchResultViewModel : ViewModel() {


    private val repo = MainRepo()

    var individualSearchResult = repo.individualSearchData

    var endHasBeenReached = repo.endHasBeenReached

    fun getIndividualSearch(postingModel: IndividualSearchResultPostingModel, searchItem: IndividualSearchInterface) = repo.getIndividualSearchResult(postingModel,searchItem)

}