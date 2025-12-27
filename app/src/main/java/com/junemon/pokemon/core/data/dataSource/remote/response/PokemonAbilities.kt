package com.junemon.pokemon.core.data.dataSource.remote.response

import com.squareup.moshi.Json


/**
 * Created by Ian Damping on 08,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */

data class PokemonAbilitiesResponse(
    @Json(name = "ability") val abilities: PokemonAbilitiesNameResponse
)

data class PokemonAbilitiesNameResponse(
    @Json(name = "name") val abilityName: String
)