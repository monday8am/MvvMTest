package com.monday8am.baseapp.domain

import androidx.lifecycle.MutableLiveData
import com.monday8am.baseapp.domain.Result.Success
/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

fun <V, V2> Result<V>.flatMap(transformValue: (V) -> Result<V2>): Result<V2> = when (this) {
    is Success<V> -> transformValue(data)
    is Result.Error -> this
}

fun <V, V2> Result<V>.map(transformValue: (V) -> V2): Result<V2> = when (this) {
    is Success<V> -> Success(transformValue(data))
    is Result.Error -> this
}

/**
 * [Success.data] if [Result] is of type [Success]
 */
fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.value = data
    }
}
