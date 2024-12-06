package org.micaklus.user.onlineuser.ui

import org.micaklus.user.onlineuser.domain.model.Post

sealed interface PostEvent {
    data class RefreshPosts(val userId: Int) : PostEvent
    data class AddPost(val post: Post) : PostEvent
}