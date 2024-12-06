package org.micaklus.user.onlineuser.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.micaklus.user.onlineuser.domain.model.Post
import org.micaklus.user.onlineuser.domain.repository.PostRepository
import si.mitja.domain.common.MyResult

class PostViewModel(private val postsRepository: PostRepository) : ScreenModel {

    private val _state = MutableStateFlow<MyResult<List<Post>>>(MyResult.Pending)
    val state = _state.asStateFlow()

    private val _create_state = MutableStateFlow<MyResult<Post>>(MyResult.Pending)
    val createState = _create_state.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()


    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.RefreshPosts -> getPosts(event.userId)
            is PostEvent.AddPost -> validateAndAddPost(event.post)
        }
    }

    private fun getPosts(userid: Int) {
        _state.update { MyResult.Loading }
        screenModelScope.launch {
            _state.update { postsRepository.getPostsByUserId(userid) }
        }
    }

    private fun createPost(post: Post) {
        _create_state.update { MyResult.Loading }
        screenModelScope.launch {
            _create_state.update { postsRepository.createPost(post) }
        }
    }

    private fun validateAndAddPost(post: Post) {
        val errors = validatePost(post)

        if (errors.isEmpty()) {
            createPost(post)
        } else {
            _validationErrors.value = errors
        }
    }

    private fun validatePost(post: Post): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (post.title.isNullOrBlank()) errors["title"] = "Title cannot be empty"
        if (post.body.isNullOrBlank()) errors["body"] = "Body cannot be empty"

        return errors
    }

    fun setCreateState(pending: MyResult.Pending) {
        _create_state.update { pending }
    }
}