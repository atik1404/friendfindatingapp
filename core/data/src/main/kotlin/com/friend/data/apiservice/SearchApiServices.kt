package com.friend.data.apiservice

import com.friend.apiresponse.search.CityApiResponse
import com.friend.apiresponse.search.CountryApiResponse
import com.friend.apiresponse.search.FriendSuggestionApiResponse
import com.friend.apiresponse.search.StateApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiServices {
    @GET("v1/FetchUserSearchByUserProfileAnswers")
    suspend fun fetchFriendSuggestion(
        @Query("username") username: String,
    ): Response<FriendSuggestionApiResponse>

    @GET("api/Location/v1/countries")
    suspend fun fetchCountryList(): Response<List<CountryApiResponse>>

    @GET("api/Location/v1/regions")
    suspend fun fetchStateList(
        @Query("country") country: String,
    ): Response<List<StateApiResponse>>

    @GET("api/Location/v1/cities")
    suspend fun fetchCityList(
        @Query("region") region: String,
        @Query("country") country: String,
    ): Response<List<CityApiResponse>>
}