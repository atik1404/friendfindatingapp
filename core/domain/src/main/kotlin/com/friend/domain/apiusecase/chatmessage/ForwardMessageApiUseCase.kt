package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForwardMessageApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<ForwardMessageApiUseCase.Params, String> {

    data class Params(
        val fromUsername: String,
        val toUsernames: List<String>,
        val ids: List<String>,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> {
        return repository.forwardMessages(params)
    }
}