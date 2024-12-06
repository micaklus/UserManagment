package org.micaklus.user.onlineuser.data.network

import kotlinx.serialization.Serializable
import org.micaklus.user.onlineuser.domain.model.Post

@Serializable
data class PostRequest(val userId: Int, val title: String, val body: String) {
}

fun Post.toPostRequest(): PostRequest = PostRequest(userId = userId, title = title, body = body)

