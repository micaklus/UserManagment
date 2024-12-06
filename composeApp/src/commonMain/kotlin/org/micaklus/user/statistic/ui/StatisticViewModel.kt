package org.micaklus.user.statistic.ui

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

class StatisticViewModel (private val userRepository: UserRepository) : ScreenModel {

    private val _state = MutableStateFlow<MyResult<List<User>>>(MyResult.Pending)
    val state = _state.asStateFlow()

    fun onEvent(event: StatisticEvent){
        when(event){
            is StatisticEvent.RefreshUsers -> getUsers()
        }
    }

    private fun getUsers() {
        _state.update {  MyResult.Loading }
        screenModelScope.launch {
            _state.update { userRepository.getUsers()}
        }
    }

}