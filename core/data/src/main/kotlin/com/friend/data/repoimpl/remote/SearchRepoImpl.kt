package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.apiservice.SearchApiServices
import com.friend.data.mapper.mapFromApiResponse
import com.friend.data.mapper.search.FriendSuggestionApiMapper
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.SearchRepository
import com.friend.entity.search.FriendSuggestionApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: SearchApiServices,
    private val friendSuggestionApiMapper: FriendSuggestionApiMapper,
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


}