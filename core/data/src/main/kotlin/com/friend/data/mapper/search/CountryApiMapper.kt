package com.friend.data.mapper.search

import com.friend.apiresponse.search.CountryApiResponse
import com.friend.data.mapper.Mapper
import com.friend.entity.search.CountryApiEntity
import javax.inject.Inject

class CountryApiMapper @Inject constructor() :
    Mapper<List<CountryApiResponse>, List<CountryApiEntity>> {

    override fun mapFromApiResponse(response: List<CountryApiResponse>): List<CountryApiEntity> {
        return response.map { api ->
            CountryApiEntity(
                name = api.text.orEmpty(),
                value = api.value.orEmpty(),
            )
        }
    }
}
