package com.junemon.pokemon.ui.state

data class UiState<T>(
    val apiState: ApiStates,
    val data: T?,
    val errorMessage: String
) {
    companion object {
        fun <T> initialize(): UiState<T> {
            return UiState(
                apiState = ApiStates.IDLE,
                data = null,
                errorMessage = ""
            )
        }
    }
}
