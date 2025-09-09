package com.friendfinapp.dating.ui.landingpage.fragments.serachfragment

interface SearchInterface {

    fun  onProductLoading()
    fun onProductLoadingFinish()
    fun onProductLoadError(message: String)
}