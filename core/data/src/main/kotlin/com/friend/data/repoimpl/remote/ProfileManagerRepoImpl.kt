package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ProfileManagerApiServices
import com.friend.data.mapper.credential.CommonApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.data.mapper.profilemanager.CacheProfile
import com.friend.data.mapper.profilemanager.OtherProfileApiMapper
import com.friend.data.mapper.profilemanager.ProfileApiMapper
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.entity.profilemanager.OtherProfileApiEntity
import com.friend.entity.profilemanager.ProfileApiEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileManagerRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ProfileManagerApiServices,
    private val cacheProfile: CacheProfile,
    private val profileApiMapper: ProfileApiMapper,
    private val commonApiMapper: CommonApiMapper,
    private val otherProfileApiMapper: OtherProfileApiMapper,
) : ProfileManageRepository {
    override suspend fun fetchProfile(): Flow<ApiResult<ProfileApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchProfile()
            }, mapper = profileApiMapper
        ).map {
            if (it is ApiResult.Success) {
                cacheProfile.cacheProfile(it.data)
            }
            it
        }
    }

    override suspend fun otherProfile(username: String): Flow<ApiResult<OtherProfileApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchOtherProfile(
                    username
                )
            }, mapper = otherProfileApiMapper
        )
    }

    override suspend fun performProfileUpdate(params: PostProfileUpdateApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performProfileUpdate(
                    params
                )
            }, mapper = commonApiMapper
        )
    }
}