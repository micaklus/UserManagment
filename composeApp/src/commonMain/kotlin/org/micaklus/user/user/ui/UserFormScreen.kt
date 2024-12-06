package org.micaklus.user.user.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.micaklus.user.common.LoadingScreen
import org.micaklus.user.common.ValidatedTextField
import org.micaklus.user.user.domain.model.Gender
import org.micaklus.user.user.domain.model.User
import si.mitja.domain.common.MyResult


class UserFormScreen(private val initialUser: User? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<FormUserViewModel>()
        val state by viewModel.state.collectAsState()
        val validationErrors by viewModel.validationErrors.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        when (val currentState = state) {
            is MyResult.Error -> { /* Handle Error */
            }

            MyResult.Empty -> {}
            MyResult.Loading -> LoadingScreen()
            MyResult.Pending -> {}
            is MyResult.Success -> navigator.pop()
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            topBar = {
                TopAppBar(
                    title = { Text(if (initialUser == null) "Add User" else "Edit User") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            UserFormContent(
                initialUser = initialUser,
                onEvent = viewModel::onEvent,
                innerPadding = innerPadding,
                validationErrors = validationErrors
            )
        }
    }
}

@Composable
private fun UserFormContent(
    initialUser: User?,
    onEvent: (FormUserEvent) -> Unit,
    innerPadding: PaddingValues,
    validationErrors: Map<String, String>
) {
    // Pre-fill the form for edit mode
    var name by remember { mutableStateOf(initialUser?.name ?: "") }
    var surname by remember { mutableStateOf(initialUser?.surname ?: "") }
    var age by remember { mutableStateOf(initialUser?.age?.toString() ?: "0") }
    var selectedGender by remember { mutableStateOf(Gender.values()[initialUser?.gender ?: 0]) }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)
    ) {
        ValidatedTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            isError = validationErrors.containsKey("name"),
            errorMessage = validationErrors["name"]
        )
        Spacer(modifier = Modifier.padding(10.dp))
        ValidatedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = "Surname",
            isError = validationErrors.containsKey("surname"),
            errorMessage = validationErrors["surname"]
        )
        Spacer(modifier = Modifier.padding(10.dp))
        ValidatedTextField(
            value = age,
            onValueChange = { age = it },
            label = "Age",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = validationErrors.containsKey("age"),
            errorMessage = validationErrors["age"]
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text("Gender")
            Gender.values().forEach { gender ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Text(text = gender.name)
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(onClick = {
                val event = if (initialUser == null) {
                    FormUserEvent.AddUser(
                        User(null, name, surname, age.toIntOrNull(), selectedGender.ordinal)
                    )
                } else {
                    FormUserEvent.EditUser(
                        User(
                            initialUser.id,
                            name,
                            surname,
                            age.toIntOrNull(),
                            selectedGender.ordinal
                        )
                    )
                }
                onEvent(event)
            }) {
                Text(if (initialUser == null) "Add User" else "Edit User")
            }
        }
    }
}



