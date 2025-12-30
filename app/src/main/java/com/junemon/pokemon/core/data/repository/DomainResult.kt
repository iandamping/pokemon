package com.junemon.pokemon.core.data.repository

sealed interface DomainResult<out T> {
    data class Data<out T>(val data: T) : DomainResult<T>

    data class Error(val message: String) : DomainResult<Nothing>
}
