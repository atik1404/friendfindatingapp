package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.domain.repository.remote.CredentialRepository
import javax.inject.Inject

class CredentialRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource
) : CredentialRepository {

}