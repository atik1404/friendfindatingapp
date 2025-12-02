package com.friend.data.apiservice

import com.friend.apiresponse.search.FriendSuggestionApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiServices {
    @GET("v1/FetchUserSearchByUserProfileAnswers")
    suspend fun fetchFriendSuggestion(
        @Query("username") username: String,
    ): Response<FriendSuggestionApiResponse>
}