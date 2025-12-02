package com.friend.domain.apiusecase.search

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.search.FriendSuggestionApiEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchFriendSuggestionApiUseCase @Inject constructor(
    private val repository: SearchRepository,
) : ApiUseCaseParams<String, List<FriendSuggestionApiEntity>> {

    override suspend fun execute(
        params: String,
    ): Flow<ApiResult<List<FriendSuggestionApiEntity>>> {
        return repository.fetchFriendsSuggestion(params)
    }
}
