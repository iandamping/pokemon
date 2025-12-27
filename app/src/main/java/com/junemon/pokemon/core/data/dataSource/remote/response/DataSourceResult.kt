package com.junemon.pokemon.core.data.dataSource.remote.response

sealed interface DataSourceResult<out T> {
    data class Data<out T>(val data: T) : DataSourceResult<T>

    data class Error(val message: String) : DataSourceResult<Nothing>
}