package com.junemon.pokemon.core.data.dataSource.remote.response

import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonResultsResponse
import com.squareup.moshi.Json

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
data class PokemonMainResponse(
    @Json(name = "results") val pokemonResults: List<PokemonResultsResponse>
)

