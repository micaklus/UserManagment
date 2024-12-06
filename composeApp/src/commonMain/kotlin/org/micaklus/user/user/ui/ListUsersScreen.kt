package org.micaklus.user.user.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.micaklus.user.common.EmptyPage
import org.micaklus.user.common.LoadingScreen
import org.micaklus.user.home.ui.HomeEvent
import org.micaklus.user.statistic.ui.StatisticScreen
import si.mitja.domain.common.MyResult

class ListUsersScreen : Screen {
    @Composable
    override fun Content() {
        val navigators = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<UserViewModel>()
        val state by viewModel.state.collectAsState()


        LaunchedEffect(Unit) {
            viewModel.onEvent(HomeEvent.RefreshUsers)
        }

        Column(Modifier.background(Color.White).padding()) {
            when (val currentstate = state) {
                is MyResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        items(currentstate.data) { user ->
                            ListItem(headlineContent = { Text("${user.name ?: ""} ${user.surname ?: ""}") },
                                supportingContent = { Text("Age: ${user.age ?: ""} Gender: ${user.getGenderEnum()}") },
                                shadowElevation = 6.dp,
                                trailingContent = {
                                    Row {
                                        Icon(
                                            Icons.Filled.Edit,
                                            contentDescription = "Edit user",
                                            modifier = Modifier.clickable {

                                                navigators.push(UserFormScreen(user))

                                            }
                                        )
                                        Spacer(modifier = Modifier.padding(10.dp))
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Delete user",
                                            modifier = Modifier.clickable {
                                                user.id?.let {
                                                    viewModel.onEvent(
                                                        HomeEvent.DeleteUser(
                                                            it
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }
                                })
                        }
                    }
                }

                is MyResult.Loading -> {
                    LoadingScreen()
                }

                MyResult.Empty -> {
                    EmptyPage(
                        message = "There is no users yet, please add one",
                        buttonText = "Add user",
                        onRetry = { navigators.push(UserFormScreen()) }
                    )
                }

                is MyResult.Error,
                MyResult.Pending -> {

                }
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { navigators.push(StatisticScreen()) },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Statistic")
                }
                Button(
                    onClick = { navigators.push(UserFormScreen()) },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Add user")
                }
            }
        }
    }
}