package com.friendfinapp.dating.di

import com.friend.data.repoimpl.remote.ChatMessageRepoImpl
import com.friend.data.repoimpl.remote.CredentialRepoImpl
import com.friend.data.repoimpl.remote.ProfileManagerRepoImpl
import com.friend.data.repoimpl.remote.SearchRepoImpl
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.repository.remote.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCredentialRepository(repoImpl: CredentialRepoImpl): CredentialRepository

    @Binds
    fun bindSearchRepository(repoImpl: SearchRepoImpl): SearchRepository

    @Binds
    fun bindProfileManagerRepository(repoImpl: ProfileManagerRepoImpl): ProfileManageRepository

    @Binds
    fun bindChatMessageRepository(repoImpl: ChatMessageRepoImpl): ChatMessagesRepository
}