package com.kl3jvi.yonda.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    data class Error(val exception: Throwable? = null) : Result<Nothing>

    object Loading : Result<Nothing>
}

/**
 * Part of Now In Android google Sample
 */
private fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return map<T, Result<T>> {
        Result.Success(it)
    }.onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error(it))
    }
}

// A function that takes a Flow<T> and returns a Flow<R>
fun <T, R> Flow<T>.convertToResultAndMapTo(transform: suspend (value: Result<T>) -> R): Flow<R> = asResult().map(transform)
