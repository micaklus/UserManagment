package org.micaklus.user.onlineuser.data.repository

import org.micaklus.user.common.CustomError
import org.micaklus.user.onlineuser.data.network.JsonPlaceholderApi
import org.micaklus.user.onlineuser.data.network.NetworkResult
import org.micaklus.user.onlineuser.data.network.toPost
import org.micaklus.user.onlineuser.data.network.toPostRequest
import org.micaklus.user.onlineuser.domain.model.Post
import org.micaklus.user.onlineuser.domain.repository.PostRepository
import si.mitja.domain.common.MyResult

class PostRepositoryImpl(val jsonPlaceholderApi: JsonPlaceholderApi) : PostRepository {

    override suspend fun getPostsByUserId(userId: Int): MyResult<List<Post>> {
        val result = jsonPlaceholderApi.getUserPosts(userId)
        when (result) {
            is NetworkResult.Error -> return MyResult.Error(
                CustomError(
                    result.error.message.toString(),
                    result.statusCode
                )
            )

            is NetworkResult.Success -> return MyResult.Success(result.data.map { it.toPost() })
        }
    }

    override suspend fun createPost(post: Post): MyResult<Post> {
        val result = jsonPlaceholderApi.createPost(post.toPostRequest())
        when (result) {
            is NetworkResult.Error -> return MyResult.Error(
                CustomError(
                    result.error.message.toString(),
                    result.statusCode
                )
            )

            is NetworkResult.Success -> return MyResult.Success(result.data.toPost())
        }
    }
}