package com.junemon.pokemon.core.data.dataSource.remote.response

import com.squareup.moshi.Json


data class PokemonResultsResponse(
    @Json(name = "url") val pokemonUrl: String
)