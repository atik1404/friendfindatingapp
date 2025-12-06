package com.friend.domain.apiusecase.search

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.search.CityApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCityApiUseCase @Inject constructor(
    private val repository: SearchRepository,
) : ApiUseCaseParams<FetchCityApiUseCase.Params, List<CityApiEntity>> {
    data class Params(
        val country: String,
        val state: String
    )

    override suspend fun execute(params: Params): Flow<ApiResult<List<CityApiEntity>>> =
        repository.fetchCityList(
            country = params.country,
            state = params.state
        )
}