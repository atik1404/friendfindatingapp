package com.friend.domain.apiusecase.search

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.search.StateApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchStateApiUseCase @Inject constructor(
    private val repository: SearchRepository,
) : ApiUseCaseParams<String, List<StateApiEntity>> {

    override suspend fun execute(params: String): Flow<ApiResult<List<StateApiEntity>>> =
        repository.fetchStateList(params)

}