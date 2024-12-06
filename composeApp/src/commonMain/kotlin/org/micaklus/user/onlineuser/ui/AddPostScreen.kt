package org.micaklus.user.onlineuser.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.micaklus.user.common.ErrorPage
import org.micaklus.user.common.LoadingScreen
import org.micaklus.user.common.ValidatedTextField
import org.micaklus.user.onlineuser.domain.model.Post
import si.mitja.domain.common.MyResult

class AddPostScreen(val userId: Int) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<PostViewModel>()
        val createState by viewModel.createState.collectAsState()
        val validationErrors by viewModel.validationErrors.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        when (val currentState = createState) {
            is MyResult.Error -> {
                ErrorPage(
                    title = "Error",
                    message = currentState.customError.message,
                    code = currentState.customError.code.toString(),
                    onRetry = {
                        viewModel.setCreateState(MyResult.Pending)
                    }
                )
            }

            MyResult.Loading -> {
                LoadingScreen()
            }

            MyResult.Pending,
            MyResult.Empty -> {
            }

            is MyResult.Success -> navigator.pop()
        }


        Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding(), topBar = {
            TopAppBar(title = {
                Text("Add post")
            },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
        ) { innerPadding ->
            AddPostContent(
                onEvent = viewModel::onEvent,
                innerPadding,
                userId,
                validationErrors
            )
        }
    }
}

@Composable
private fun AddPostContent(
    onEvent: (PostEvent) -> Unit,
    innerPadding: PaddingValues,
    userId: Int,
    validationErrors: Map<String, String>
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)
    ) {
        ValidatedTextField(
            value = title,
            onValueChange = { title = it },
            label = "Title",
            isError = validationErrors.containsKey("title"),
            errorMessage = validationErrors["title"]

        )
        Spacer(modifier = Modifier.padding(10.dp))
        ValidatedTextField(
            value = body,
            onValueChange = { body = it },
            label = "Body",
            isError = validationErrors.containsKey("body"),
            errorMessage = validationErrors["body"]

        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = {
            onEvent(
                PostEvent.AddPost(
                    Post(title = title, body = body, userId = userId)
                )
            )
        })
        {
            Text("Add Post")
        }
    }
}

