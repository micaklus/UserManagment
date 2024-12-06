package org.micaklus.user.onlineuser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.micaklus.user.common.ErrorPage
import org.micaklus.user.common.LoadingScreen
import si.mitja.domain.common.MyResult

class PostsScreen(val userId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigators = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<PostViewModel>()
        val state by viewModel.state.collectAsState()


        LaunchedEffect(Unit) {
            viewModel.onEvent(PostEvent.RefreshPosts(userId))
        }

        Column() {
            when (val currentstate = state) {
                is MyResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentstate.data) { post ->
                            ListItem(headlineContent = { Text(post.title ?: "") },
                                supportingContent = { Text(post.body ?: "") },
                                shadowElevation = 6.dp,
                                leadingContent = {
                                    Row {
                                        Icon(
                                            Icons.Filled.Menu,
                                            contentDescription = "Post for user",
                                        )
                                    }

                                })
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
                        onRetry = { viewModel.onEvent(PostEvent.RefreshPosts(userId)) }
                    )
                }

                MyResult.Empty -> {

                }

                MyResult.Pending -> {

                }
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { navigators.push(AddPostScreen(userId)) },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Add post")
                }
            }
        }
    }
}