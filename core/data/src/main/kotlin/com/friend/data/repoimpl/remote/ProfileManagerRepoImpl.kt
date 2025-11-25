package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.apiservice.ProfileManagerApiServices
import com.friend.domain.repository.remote.ProfileManageRepository
import javax.inject.Inject

class ProfileManagerRepoImpl  @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ProfileManagerApiServices
) : ProfileManageRepository {

}