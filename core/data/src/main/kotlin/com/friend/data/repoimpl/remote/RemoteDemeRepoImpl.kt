package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.domain.repository.remote.RemoteDemoRepository
import javax.inject.Inject

class RemoteDemeRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource
) : RemoteDemoRepository {

}