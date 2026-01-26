package com.friend.data.mapper.search

import com.friend.apiresponse.search.FriendSuggestionApiResponse
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppFileBaseUrl
import com.friend.entity.search.FriendSuggestionApiEntity
import jakarta.inject.Inject

class FriendSuggestionApiMapper @Inject constructor() :
    Mapper<FriendSuggestionApiResponse, List<FriendSuggestionApiEntity>> {

    @Inject
    @AppFileBaseUrl
    lateinit var imageBaseUrl: String


    override fun mapFromApiResponse(response: FriendSuggestionApiResponse): List<FriendSuggestionApiEntity> {
        return response.data?.map { item ->
            FriendSuggestionApiEntity(
                username = item.username.orEmpty(),
                fullName = item.fullName.orEmpty(),
                userImage = if (item.userImage != null && item.userImage?.isNotEmpty() == true) "$imageBaseUrl${item.userImage}" else "",
            )
        } ?: emptyList()
    }
}