package com.friendfinapp.dating.ui.individualsearch

interface IndividualSearchInterface {

    fun  onProductLoading()
    fun onProductLoadingFinish()
    fun onProductLoadError(message: String)
}