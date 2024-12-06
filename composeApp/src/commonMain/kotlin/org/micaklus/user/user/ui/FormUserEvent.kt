package org.micaklus.user.user.ui

import org.micaklus.user.user.domain.model.User

sealed interface FormUserEvent {
    data class AddUser(val user: User) : FormUserEvent
    data class EditUser(val user: User) : FormUserEvent
}