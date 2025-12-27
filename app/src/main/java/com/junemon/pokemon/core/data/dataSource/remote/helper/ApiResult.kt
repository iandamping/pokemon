package com.junemon.pokemon.core.data.dataSource.remote.helper

sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>

    data class Error(
        val message: String,
        val code: Int? = null,
        val cause: Exception? = null
    ) : ApiResult<Nothing>
}
