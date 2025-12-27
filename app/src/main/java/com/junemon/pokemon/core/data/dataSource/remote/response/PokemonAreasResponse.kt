package com.junemon.pokemon.core.data.dataSource.remote.response

import com.squareup.moshi.Json

data class PokemonAreasResponse(
    @Json(name = "location_area") val area: PokemonAreasName
)

data class PokemonAreasName(
    @Json(name = "name") val name: String
)
