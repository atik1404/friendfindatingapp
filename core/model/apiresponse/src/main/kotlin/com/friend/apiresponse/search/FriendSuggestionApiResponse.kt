package com.friend.apiresponse.search

data class FriendSuggestionApiResponse(
    val status_code: Int?,
    val message: String?,
    val data: List<FriendSuggestionApiItemResponse>?,
    val count: Int?
)

data class FriendSuggestionApiItemResponse(
    val username: String?,
    val userimage: String?
)