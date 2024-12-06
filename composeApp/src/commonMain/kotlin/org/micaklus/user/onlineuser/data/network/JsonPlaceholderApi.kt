package org.micaklus.user.onlineuser.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class JsonPlaceholderApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }


    suspend fun getAllUsers(): NetworkResult<List<UserResponse>> {
        return if (isNetworkAvailable()) {
            try {
                httpClient.get("https://jsonplaceholder.typicode.com/users").toResult()
            } catch (e: Exception) {
                NetworkResult.Error(NetworkException("Failed to fetch users: ${e.message}"))
            }
        } else {
            NetworkResult.Error(NetworkException("No internet connection"))
        }
    }

    suspend fun getUserPosts(userId: Int): NetworkResult<List<PostResponse>> {
        return if (isNetworkAvailable()) {
            try {
                httpClient.get("https://jsonplaceholder.typicode.com/user/${userId}/posts")
                    .toResult()
            } catch (e: Exception) {
                NetworkResult.Error(NetworkException("Failed to fetch users: ${e.message}"))
            }
        } else {
            NetworkResult.Error(NetworkException("No internet connection"))
        }
    }

    suspend fun createPost(post: PostRequest): NetworkResult<PostResponse> {
        return if (isNetworkAvailable()) {
            try {
                httpClient.post("https://jsonplaceholder.typicode.com/posts") {
                    contentType(ContentType.Application.Json)
                    setBody(post)
                }.toResult()

            } catch (e: Exception) {
                NetworkResult.Error(NetworkException("Failed to fetch users: ${e.message}"))
            }
        } else {
            NetworkResult.Error(NetworkException("No internet connection"))
        }

    }
}