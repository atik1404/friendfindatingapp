package com.friend.domain.repository.remote

import com.friend.domain.base.ApiResult
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.FriendSuggestionApiEntity
import com.friend.entity.search.StateApiEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun fetchFriendsSuggestion(userName: String): Flow<ApiResult<List<FriendSuggestionApiEntity>>>

    suspend fun fetchCountryList(): Flow<ApiResult<List<CountryApiEntity>>>

    suspend fun fetchStateList(country: String): Flow<ApiResult<List<StateApiEntity>>>

    suspend fun fetchCityList(
        country: String,
        state: String
    ): Flow<ApiResult<List<CityApiEntity>>>
}