package org.micaklus.user.user.ui


import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.micaklus.user.user.domain.model.User
import org.micaklus.user.user.domain.repository.UserRepository
import si.mitja.domain.common.MyResult

class FormUserViewModel(private val userRepository: UserRepository) : ScreenModel {

    private val _state = MutableStateFlow<MyResult<Unit>>(MyResult.Pending)
    val state: StateFlow<MyResult<Unit>> = _state.asStateFlow()
    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    fun onEvent(event: FormUserEvent) {
        when (event) {
            is FormUserEvent.AddUser -> {
                validateAndAddUser(event.user)
            }

            is FormUserEvent.EditUser -> {
                validateAndEditUser(event.user)
            }
        }
    }

    private fun validateAndEditUser(user: User) {
        val errors = validateUser(user)

        if (errors.isEmpty()) {
            editUser(user)
        } else {
            _validationErrors.value = errors
        }
    }

    private fun validateAndAddUser(user: User) {
        val errors = validateUser(user)

        if (errors.isEmpty()) {
            addUser(user)
        } else {
            _validationErrors.value = errors
        }
    }

    private fun validateUser(user: User): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (user.name.isNullOrBlank()) errors["name"] = "Name cannot be empty"
        if (user.surname.isNullOrBlank()) errors["surname"] = "Surname cannot be empty"
        if (user.age == null || user.age <= 0 || user.age > 150) errors["age"] =
            "Age must be a positive number between 1 and 150"

        return errors
    }


    private fun editUser(user: User) {
        _state.value = MyResult.Loading
        screenModelScope.launch {
            _state.value = userRepository.updateUser(user)
        }
    }

    private fun addUser(user: User) {
        _state.value = MyResult.Loading
        screenModelScope.launch {
            _state.value = userRepository.createUser(user)
        }
    }


}