package org.micaklus.user.user.data.local

import org.micaklus.user.user.domain.model.User

data class UserDto(val id: Long?, val name: String?, val surname: String?, val age: Int?, val gender: Int?) {


    fun toModel(): User {
        return User(id, name, surname, age, gender)
    }

    companion object {
        fun fromModel(user: User): UserDto {
            return UserDto(user.id, user.name , user.surname , user.age, user.gender)
        }
    }
}