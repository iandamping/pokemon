package com.junemon.pokemon.util.pokemonColorExtractor

import androidx.compose.ui.graphics.Color
import coil3.Image

interface PokemonColorExtractor {

    suspend fun extractColorFromImage(pokemonId: String, image: Image): Color
}
