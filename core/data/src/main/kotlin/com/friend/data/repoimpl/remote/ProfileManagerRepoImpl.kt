package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.domain.repository.remote.ProfileManageRepository
import javax.inject.Inject

class ProfileManagerRepoImpl  @Inject constructor(
    private val networkBoundResources: NetworkBoundResource
) : ProfileManageRepository {

}