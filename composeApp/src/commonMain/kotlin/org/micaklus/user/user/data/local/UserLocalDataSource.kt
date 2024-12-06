package org.micaklus.user.user.data.local

import app.cash.sqldelight.db.SqlDriver
import org.micaklus.user.cache.Database

class UserLocalDataSource(private val sqlDriver: SqlDriver) {
    private val database = Database(sqlDriver)

    suspend fun getUsers(): List<UserDto> {
        return database.getAllUsers()
    }

    suspend fun getUserById(userId: Long): UserDto {
        return database.getUserById(userId) ?: throw Exception("User not found")
    }

    suspend fun createUser(user: UserDto) {
        database.insertUser(user)
    }

    suspend fun updateUser(user: UserDto): UserDto {
        database.updateUser(user)
        return user.id?.let { database.getUserById(it) } ?: throw Exception("User not found")
    }

    suspend fun deleteUser(userId: Long): Unit {
        database.deleteUser(userId)
    }
}