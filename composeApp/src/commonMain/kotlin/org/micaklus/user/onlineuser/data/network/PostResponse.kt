package org.micaklus.user.onlineuser.data.network

import kotlinx.serialization.Serializable
import org.micaklus.user.onlineuser.domain.model.Post

@Serializable
data class PostResponse(val userId: Int, val id: Int, val title: String, val body: String) {
}

fun PostResponse.toPost(): Post = Post(userId = userId, id = id, title = title, body = body)

