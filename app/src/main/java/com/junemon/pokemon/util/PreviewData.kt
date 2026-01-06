package com.junemon.pokemon.util

import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonStat

object PreviewData {
    val PreviewPokemonDetail: PokemonDetail = PokemonDetail(
        pokemonId = 1,
        pokemonName = "Bulbasaur",
        pokemonWeight = 69,
        pokemonHeight = 7,
        pokemonImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        pokemonAbilities = listOf("Overgrow", "Chlorophyll"),
        pokemonSmallImages = listOf(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
        ),
        pokemonStats = listOf(
            PokemonStat(point = 45, name = "Hp"),
            PokemonStat(point = 49, name = "Attack"),
            PokemonStat(point = 49, name = "Defense"),
            PokemonStat(point = 65, name = "Special - attack"),
            PokemonStat(point = 65, name = "Special - defense"),
            PokemonStat(point = 45, name = "Speed"),
        ),
        pokemonTypes = listOf("Grass", "Poison"),
        pokemonSpeciesUrl = "https://pokeapi.co/api/v2/pokemon-species/1/"
    )
}
