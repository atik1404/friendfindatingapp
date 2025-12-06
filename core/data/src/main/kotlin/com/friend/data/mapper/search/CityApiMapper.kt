package com.friend.data.mapper.search

import com.friend.apiresponse.search.CityApiResponse
import com.friend.data.mapper.Mapper
import com.friend.entity.search.CityApiEntity
import javax.inject.Inject

class CityApiMapper @Inject constructor() :
    Mapper<List<CityApiResponse>, List<CityApiEntity>> {

    override fun mapFromApiResponse(response: List<CityApiResponse>): List<CityApiEntity> {
        return response.map { api ->
            CityApiEntity(
                name = api.text.orEmpty(),
                value = api.value.orEmpty(),
            )
        }
    }
}