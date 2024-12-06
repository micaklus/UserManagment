package org.micaklus.user.onlineuser.domain.repository

import org.micaklus.user.onlineuser.domain.model.Post
import si.mitja.domain.common.MyResult

interface PostRepository {
    suspend fun getPostsByUserId(userId: Int): MyResult<List<Post>>
    suspend fun createPost(post: Post): MyResult<Post>
}