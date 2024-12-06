package org.micaklus.user.onlineuser.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.micaklus.user.common.ErrorPage
import org.micaklus.user.common.LoadingScreen
import si.mitja.domain.common.MyResult

class ListOnlineUsersScreen : Screen {
    @Composable
    override fun Content() {
        val navigators = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<OnlineUserViewModel>()
        val state by viewModel.state.collectAsState()


        LaunchedEffect(Unit) {
            viewModel.onEvent(OnlineUserEvent.RefreshUsers)
        }

        Column(Modifier.padding()) {
            when (val currentstate = state) {
                is MyResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentstate.data) { user ->
                            ListItem(headlineContent = { Text("Name: ${user.name ?: ""}\nUsername: ${user.username ?: ""}") },
                                supportingContent = { Text("Email: ${user.email ?: ""}\nCompany: ${user.company.name}\nPhone: ${user.phone}") },
                                shadowElevation = 6.dp,
                                trailingContent = {
                                    Row {
                                        Icon(
                                            Icons.Filled.Email,
                                            contentDescription = "Post for user",
                                        )
                                    }

                                },
                                modifier = Modifier.clickable {
                                    navigators.push(PostsScreen(user.id))
                                }
                            )
                        }
                    }
                }

                is MyResult.Loading -> {
                    LoadingScreen()
                }

                is MyResult.Error -> {
                    ErrorPage(
                        title = "Error",
                        message = currentstate.customError.message,
                        code = currentstate.customError.code.toString(),
                        onRetry = { viewModel.onEvent(OnlineUserEvent.RefreshUsers) }
                    )
                }

                MyResult.Empty,
                MyResult.Pending -> {
                    //Do Nothing
                }
            }
        }
    }
}