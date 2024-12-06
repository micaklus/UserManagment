package org.micaklus.user.home.ui

import org.micaklus.user.user.domain.model.User

sealed interface HomeEvent {

    data object RefreshUsers : HomeEvent
    data class DeleteUser(val id: Long) : HomeEvent
    data class EditUser(val user: User) : HomeEvent
}