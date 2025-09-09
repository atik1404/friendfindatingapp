package com.friendfinapp.dating

import retrofit2.http.GET
import retrofit2.http.Query

interface GeonamesApi {
    @GET("childrenJSON")
    suspend fun getStatesForCountry(@Query("geonameId") countryCode: String): List<StateResponse>

    @GET("searchJSON")
    suspend fun getCitiesForState(@Query("adminCode1") stateCode: String): List<CityResponse>
}

data class StateResponse(
    val adminName1: String // State name (e.g., "California")
)

data class CityResponse(
    val name: String // City name (e.g., "Los Angeles")
)
