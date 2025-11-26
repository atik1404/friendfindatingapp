package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.mapper.credential.LoginApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CredentialRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: CredentialApiServices,
    private val loginApiMapper: LoginApiMapper,
) : CredentialRepository {
    override suspend fun postLoginApi(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.login(
                    params
                )
            }, mapper = loginApiMapper
        )
    }
}