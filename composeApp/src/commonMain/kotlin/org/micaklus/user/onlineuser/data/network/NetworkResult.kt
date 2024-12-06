package org.micaklus.user.onlineuser.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed interface NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>
    data class Error<out T : Any>(val error: Exception, val statusCode: Int? = null) :
        NetworkResult<T>
}

expect fun isNetworkAvailable(): Boolean

suspend inline fun <reified T : Any> HttpResponse.toResult(): NetworkResult<T> {
    return when (status.value) {
        200 -> NetworkResult.Success(body())
        400 -> NetworkResult.Error(
            NetworkException("Check your credentials and try again!"),
            status.value
        )

        401 -> NetworkResult.Error(
            NetworkException("Authorization Failed! Try Logging In again."),
            status.value
        )

        500, 503 -> NetworkResult.Error(
            NetworkException("Server Disruption! We are on fixing it."),
            status.value
        )

        504 -> NetworkResult.Error(
            NetworkException("Too much load at this time, try again later!"),
            status.value
        )

        else -> NetworkResult.Error(
            NetworkException("Something went wrong! Please try again or contact support."),
            status.value
        )
    }
}

class NetworkException(message: String, code: Int? = null) : Exception(message)
