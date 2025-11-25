package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.apiservice.SearchApiServices
import com.friend.domain.repository.remote.SearchRepository
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: SearchApiServices
) : SearchRepository {

}