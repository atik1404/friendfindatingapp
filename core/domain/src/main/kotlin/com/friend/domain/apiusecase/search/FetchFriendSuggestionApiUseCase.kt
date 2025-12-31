package com.friend.domain.apiusecase.search

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.search.FriendSuggestionApiEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchFriendSuggestionApiUseCase @Inject constructor(
    private val repository: SearchRepository,
) : ApiUseCaseParams<FetchFriendSuggestionApiUseCase.Params, List<FriendSuggestionApiEntity>> {

    data class Params(
        val gender: Int? = null,
        val fromAge: Int? = null,
        val toAge: Int? = null,
        val country: String? = null,
        val state: String? = null,
        val city: String? = null,
        val username: String? = null,
        val isOnlineUser: String? = null,
        val bodyType: String? = null,
        val lookingFor: String? = null,
        val eyes: String? = null,
        val hair: String? = null,
        val smoking: String? = null,
        val drinking: String? = null,
        val pageNo: Int = 1,
    )

    override suspend fun execute(
        params: Params,
    ): Flow<ApiResult<List<FriendSuggestionApiEntity>>> {
        return repository.fetchFriendsSuggestion(params)
    }
}
