package com.friend.domain.apiusecase.search

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.domain.usecase.ApiUseCaseNonParams
import com.friend.entity.search.CountryApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCountriesUseCase @Inject constructor(
    private val repository: SearchRepository,
) : ApiUseCaseNonParams<List<CountryApiEntity>> {
    override suspend fun execute(): Flow<ApiResult<List<CountryApiEntity>>> =
        repository.fetchCountryList()
}