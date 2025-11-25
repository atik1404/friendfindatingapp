package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ChatMessageApiServices
import com.friend.data.apiservice.CredentialApiServices
import com.friend.domain.repository.remote.ChatMessagesRepository
import javax.inject.Inject

class ChatMessageRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ChatMessageApiServices
) : ChatMessagesRepository {

}