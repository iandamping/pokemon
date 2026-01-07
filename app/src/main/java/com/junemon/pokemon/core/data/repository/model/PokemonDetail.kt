package com.junemon.pokemon.core.data.repository.model

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
data class PokemonDetail(
    val pokemonId: Int,
    val pokemonName: String,
    val pokemonWeight: Int,
    val pokemonHeight: Int,
    val pokemonImage: String,
    val pokemonAbilities: List<String>,
    val pokemonSmallImages: List<String?>,
    val pokemonStats: List<PokemonStat>,
    val pokemonTypes: List<String>,
    val pokemonSpeciesUrl: String
)

data class PokemonStat(val point: Int, val name: String)
