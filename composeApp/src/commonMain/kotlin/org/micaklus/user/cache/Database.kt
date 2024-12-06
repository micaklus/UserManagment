package org.micaklus.user.cache

import app.cash.sqldelight.db.SqlDriver
import org.micaklus.user.user.data.local.UserDto
import sqldelight.org.micaklus.user.database.AppDatabase

public class Database(driver: SqlDriver) {
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllUsers(): List<UserDto> {
        return dbQuery.selectAllUsers().executeAsList()
            .map { UserDto(it.id, it.name, it.surname, it.age.toInt(), it.gender.toInt()) }
    }

    internal fun insertUser(user: UserDto) {
        requireNotNull(user.name) { "Name cannot be null" }
        requireNotNull(user.surname) { "Surname cannot be null" }
        requireNotNull(user.age) { "Age cannot be null" }
        requireNotNull(user.gender) { "Gender cannot be null" }

        dbQuery.insert(user.name, user.surname, user.age.toLong(), user.gender.toLong())
    }

    internal fun deleteUser(id: Long) {
        dbQuery.deleteUser(id)
    }

    internal fun updateUser(user: UserDto) {
        requireNotNull(user.id) { "ID cannot be null" }
        requireNotNull(user.name) { "Name cannot be null" }
        requireNotNull(user.surname) { "Surname cannot be null" }
        requireNotNull(user.age) { "Age cannot be null" }
        requireNotNull(user.gender) { "Gender cannot be null" }
        dbQuery.updateUser(
            user.name,
            user.surname,
            user.age.toLong(),
            user.gender.toLong(),
            user.id
        )
    }

    internal fun getUserById(id: Long): UserDto {
        val user = dbQuery.selectUserById(id).executeAsList().first()
        return UserDto(user.id, user.name, user.surname, user.age.toInt(), user.gender.toInt())
    }
}