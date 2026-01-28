package com.junemon.pokemon.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
sealed interface NavigationScreen {

    @Keep
    @Serializable
    object HomeScreen : NavigationScreen

    @Keep
    @Serializable
    data class DetailScreen(val pokemonId: Int) : NavigationScreen
}
