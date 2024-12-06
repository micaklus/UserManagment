package org.micaklus.user.onlineuser.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.micaklus.user.onlineuser.domain.model.OnlineUser
import org.micaklus.user.onlineuser.domain.repository.OnlineUserRepository
import si.mitja.domain.common.MyResult

class OnlineUserViewModel(private val userRepository: OnlineUserRepository) : ScreenModel {

    private val _state = MutableStateFlow<MyResult<List<OnlineUser>>>(MyResult.Pending)
    val state = _state.asStateFlow()

    fun onEvent(event: OnlineUserEvent) {
        when (event) {
            is OnlineUserEvent.RefreshUsers -> getUsers()
        }
    }

    private fun getUsers() {
        _state.update { MyResult.Loading }
        screenModelScope.launch {
            _state.update { userRepository.getOnlineUsers() }
        }
    }

}