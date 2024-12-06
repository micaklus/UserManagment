package org.micaklus.user.onlineuser.data.repository

import org.micaklus.user.common.CustomError
import org.micaklus.user.onlineuser.data.network.JsonPlaceholderApi
import org.micaklus.user.onlineuser.data.network.NetworkResult
import org.micaklus.user.onlineuser.data.network.toOnlineUser
import org.micaklus.user.onlineuser.domain.model.OnlineUser
import org.micaklus.user.onlineuser.domain.repository.OnlineUserRepository
import si.mitja.domain.common.MyResult

class OnlineUserRepositoryImpl(val jsonPlaceholderApi: JsonPlaceholderApi) : OnlineUserRepository {
    override suspend fun getOnlineUsers(): MyResult<List<OnlineUser>> {
        val result = jsonPlaceholderApi.getAllUsers()
        when (result) {
            is NetworkResult.Error -> return MyResult.Error(
                CustomError(
                    result.error.message.toString(),
                    result.statusCode
                )
            )

            is NetworkResult.Success -> return MyResult.Success(result.data.map { it.toOnlineUser() })
        }
    }
}