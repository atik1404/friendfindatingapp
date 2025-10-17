package com.friend.data.repoimpl.local

import com.friend.cache.dao.DemoDao
import com.friend.domain.repository.local.LocalDemoRepository
import javax.inject.Inject

class LocalLocalDemoRepoImpl @Inject constructor(
    private val demoDao: DemoDao
): LocalDemoRepository {

}