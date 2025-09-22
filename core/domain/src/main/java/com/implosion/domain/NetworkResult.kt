package com.implosion.domain

sealed class NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>()

    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()
}

// Расширение для безопасных запросов
suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Unknown error",
            code = null
        )
    }
}