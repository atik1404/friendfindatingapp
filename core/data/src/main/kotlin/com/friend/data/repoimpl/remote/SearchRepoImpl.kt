package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.SearchApiServices
import com.friend.data.mapper.mapFromApiResponse
import com.friend.data.mapper.search.CityApiMapper
import com.friend.data.mapper.search.CountryApiMapper
import com.friend.data.mapper.search.FriendSuggestionApiMapper
import com.friend.data.mapper.search.StateApiMapper
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.FriendSuggestionApiEntity
import com.friend.entity.search.StateApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: SearchApiServices,
    private val friendSuggestionApiMapper: FriendSuggestionApiMapper,
    private val cityApiMapper: CityApiMapper,
    private val stateApiMapper: StateApiMapper,
    private val countryApiMapper: CountryApiMapper,
) : SearchRepository {
    override suspend fun fetchFriendsSuggestion(userName: String): Flow<ApiResult<List<FriendSuggestionApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchFriendSuggestion(
                    userName
                )
            }, mapper = friendSuggestionApiMapper
        )
    }

    override suspend fun fetchCountryList(): Flow<ApiResult<List<CountryApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchCountryList()
            }, mapper = countryApiMapper
        )
    }

    override suspend fun fetchStateList(country: String): Flow<ApiResult<List<StateApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchStateList(country)
            }, mapper = stateApiMapper
        )
    }

    override suspend fun fetchCityList(
        country: String,
        state: String
    ): Flow<ApiResult<List<CityApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchCityList(
                    country = country,
                    region = state
                )
            }, mapper = cityApiMapper
        )
    }
}