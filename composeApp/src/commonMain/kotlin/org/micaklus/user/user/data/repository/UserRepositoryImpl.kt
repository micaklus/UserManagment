package org.micaklus.user.user.data.repository

import org.micaklus.user.common.CustomError
import org.micaklus.user.user.data.local.UserDto
import org.micaklus.user.user.data.local.UserLocalDataSource
import org.micaklus.user.user.domain.model.User
import org.micaklus.user.user.domain.repository.UserRepository
import si.mitja.domain.common.MyResult

class UserRepositoryImpl(val localDataSource: UserLocalDataSource) : UserRepository {
    override suspend fun getUsers(): MyResult<List<User>> {
        return try {
            val users = localDataSource.getUsers().map { it.toModel() }
            if (users.isEmpty()) {
                return MyResult.Empty
            }
            MyResult.Success(localDataSource.getUsers().map { it.toModel() })
        } catch (e: Exception) {
            MyResult.Error(CustomError("Error getting users"))
        }
    }

    override suspend fun getUserById(userId: Long): MyResult<User> {
        return try {
            MyResult.Success(localDataSource.getUserById(userId).toModel())
        } catch (e: Exception) {
            return MyResult.Error(CustomError("Error getting user"))
        }
    }

    override suspend fun createUser(user: User): MyResult<Unit> {
        return try {
            val userDto = UserDto.fromModel(user)
            localDataSource.createUser(userDto)
            MyResult.Success(Unit)
        } catch (e: Exception) {
            return MyResult.Error(CustomError("Error creating user"))
        }
    }

    override suspend fun updateUser(user: User): MyResult<Unit> {
        return try {
            val userDto = UserDto.fromModel(user)
            val updatedUser = localDataSource.updateUser(userDto)
            MyResult.Success(Unit)
        } catch (e: Exception) {
            return MyResult.Error(CustomError("Error updating user"))
        }
    }

    override suspend fun deleteUser(userId: Long): MyResult<Unit> {
        return try {
            localDataSource.deleteUser(userId)
            MyResult.Success(Unit)
        } catch (e: Exception) {
            MyResult.Error(CustomError("Error deleting user"))
        }
    }
}