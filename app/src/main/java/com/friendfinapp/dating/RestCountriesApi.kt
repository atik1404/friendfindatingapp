package com.friendfinapp.dating

import retrofit2.http.GET

interface RestCountriesApi {
    @GET("v3.1/all")
    suspend fun getAllCountries(): List<CountryResponse>
}

data class CountryResponse(
    val name: NameResponse,
    val alpha2Code: String // Country code (e.g., "US" for USA)
)

data class NameResponse(
    val common: String // Country name (e.g., "United States")
)

