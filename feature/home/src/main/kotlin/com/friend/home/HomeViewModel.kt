package com.friend.home

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.search.FetchFriendSuggestionApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val fetchFriendSuggestionApiUseCase: FetchFriendSuggestionApiUseCase,
) : BaseViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    init {
        _fullName.value = sharedPrefHelper.getString(SpKey.fullName)

        fetchFriendSuggestions()
    }

    private fun fetchFriendSuggestions() {
        execute {
            val userName = sharedPrefHelper.getString(SpKey.userName)
            fetchFriendSuggestionApiUseCase.execute(userName).collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {}
                }
            }
        }
    }
}