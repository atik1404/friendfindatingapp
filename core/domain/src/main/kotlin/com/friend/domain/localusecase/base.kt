package com.friend.domain.localusecase

import com.friend.domain.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

interface RoomSuspendableUseCase<Params, ReturnType> : BaseUseCase {
    suspend operator fun invoke(params: Params) : ReturnType
}

interface RoomCollectableUseCase<Params, ReturnType> : BaseUseCase {
    operator fun invoke(params: Params): Flow<ReturnType>
}