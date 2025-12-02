package com.friend.domain.repository.remote

import com.friend.domain.base.ApiResult
import com.friend.entity.search.FriendSuggestionApiEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun fetchFriendsSuggestion(userName: String): Flow<ApiResult<List<FriendSuggestionApiEntity>>>
}