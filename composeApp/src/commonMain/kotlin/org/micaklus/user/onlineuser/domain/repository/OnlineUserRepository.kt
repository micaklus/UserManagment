package org.micaklus.user.onlineuser.domain.repository

import org.micaklus.user.onlineuser.domain.model.OnlineUser
import si.mitja.domain.common.MyResult

interface OnlineUserRepository {
    suspend fun getOnlineUsers(): MyResult<List<OnlineUser>>
}