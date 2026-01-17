package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.ChatItemApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchChatListApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<Int, List<ChatItemApiEntity>> {

    override suspend fun execute(params: Int): Flow<ApiResult<List<ChatItemApiEntity>>> =
        repository.fetchChatList(params)
}