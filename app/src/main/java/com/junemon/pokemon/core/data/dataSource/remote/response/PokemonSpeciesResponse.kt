package com.junemon.pokemon.core.data.dataSource.remote.response

import com.squareup.moshi.Json

/**
 * Created by Ian Damping on 10,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
data class PokemonSpeciesResultResponse(@Json(name = "url") val speciesUrl: String)
