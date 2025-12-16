package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.ChatListItemApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchChatListApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<FetchChatListApiUseCase.Params, List<ChatListItemApiEntity>> {
    data class Params(
        val fromUsername: String,
        val pageNo: Int = 0
    )

    override suspend fun execute(params: Params): Flow<ApiResult<List<ChatListItemApiEntity>>> =
        repository.fetchChatList(params)
}