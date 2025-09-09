package com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchPostingModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.UserOnlineUpdateModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.SearchInterface
import com.friendfinapp.dating.ui.mainrepo.MainRepo

class SearchViewModel : ViewModel() {


    private val repo = MainRepo()
    var SearchData = repo.SearchData
    var searchDataWithLocationAndInterest = repo.searchDataWithLocationAndInterest


    fun observeSearchData():  LiveData<SearchResponseModel> = SearchData
    var endHasBeenReached = repo.endHasBeenReached

    fun getTopSerach(postingModel: SearchPostingModel,searchItem: SearchInterface) = repo.getTopSearch(postingModel,searchItem)
    fun getTopSearchWithLocationAndInterest(userName:String) = repo.getTopSearchWithLocationAndInterest(userName)
    fun getUserOnlineUpdate(userName:String) : LiveData<UserOnlineUpdateModel> = repo.getUserOnlineUpdate(userName)
}