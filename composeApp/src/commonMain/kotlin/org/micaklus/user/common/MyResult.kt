package si.mitja.domain.common

import org.micaklus.user.common.CustomError

sealed class MyResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : MyResult<T>()
    data class Error(val customError: CustomError) : MyResult<Nothing>()
    object Loading : MyResult<Nothing>()
    object Pending : MyResult<Nothing>()
    object Empty : MyResult<Nothing>()
}