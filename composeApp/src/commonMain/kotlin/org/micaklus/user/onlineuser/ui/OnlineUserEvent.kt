package org.micaklus.user.onlineuser.ui

sealed interface OnlineUserEvent {
    data object RefreshUsers : OnlineUserEvent
}