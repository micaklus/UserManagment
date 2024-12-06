package org.micaklus.user.user.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.micaklus.user.home.ui.HomeEvent
import org.micaklus.user.user.domain.model.User
import org.micaklus.user.user.domain.repository.UserRepository
import si.mitja.domain.common.MyResult

class UserViewModel(private val userRepository: UserRepository) : ScreenModel {

    private val _state = MutableStateFlow<MyResult<List<User>>>(MyResult.Pending)
    val state = _state.asStateFlow()

    private fun getUsers() {
        _state.update { MyResult.Loading }
        screenModelScope.launch {
            _state.update { userRepository.getUsers() }
        }
    }

    private fun deleteUser(id: Long) {
        _state.update { MyResult.Loading }
        screenModelScope.launch {
            val result = userRepository.deleteUser(id)
            if (result is MyResult.Success) {
                getUsers()
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RefreshUsers -> {
                getUsers()
            }

            is HomeEvent.DeleteUser -> deleteUser(event.id)
            is HomeEvent.EditUser -> {
            }
        }
    }
}