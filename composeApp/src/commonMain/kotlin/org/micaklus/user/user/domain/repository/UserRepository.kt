package org.micaklus.user.user.domain.repository

import org.micaklus.user.user.domain.model.User
import si.mitja.domain.common.MyResult

interface UserRepository {
    suspend fun getUsers(): MyResult<List<User>>
    suspend fun getUserById(userId: Long): MyResult<User>
    suspend fun createUser(user: User): MyResult<Unit>
    suspend fun updateUser(user: User): MyResult<Unit>
    suspend fun deleteUser(userId: Long): MyResult<Unit>


}