package com.friend.data.mapper.search

import com.friend.apiresponse.search.StateApiResponse
import com.friend.data.mapper.Mapper
import com.friend.entity.search.StateApiEntity
import javax.inject.Inject

class StateApiMapper @Inject constructor() :
    Mapper<List<StateApiResponse>, List<StateApiEntity>> {

    override fun mapFromApiResponse(response: List<StateApiResponse>): List<StateApiEntity> {
        return response.map { api ->
            StateApiEntity(
                name = api.text.orEmpty(),
                value = api.value.orEmpty(),
            )
        }
    }
}