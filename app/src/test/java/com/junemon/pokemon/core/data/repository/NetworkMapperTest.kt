package com.junemon.pokemon.core.data.repository

import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_LIST_POKEMON_ABILITY
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_LIST_POKEMON_BASIC_STAT
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_LIST_POKEMON_TYPE
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_SPECIAL_1
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_SPRITE
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonAbilitiesNameResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonAbilitiesResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonTypeSingleResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonTypesResponse
import com.junemon.pokemon.core.data.repository.model.toExternalModel
import org.junit.Assert
import org.junit.Test

class NetworkMapperTest {

    @Test
    fun `NetworkMapper toExternalModel test lowercase firstChar data`() {
        val customResponse = PokemonDetailResponse(
            pokemonId = 1,
            pokemonName = "charmander",
            pokemonWeight = 1,
            pokemonHeight = 1,
            pokemonImage = DUMMY_POKEMON_SPRITE,
            pokemonStats = DUMMY_LIST_POKEMON_BASIC_STAT,
            pokemonTypes = listOf(PokemonTypesResponse(PokemonTypeSingleResponse(typeName = "predator"))),
            pokemonAbilities = listOf(PokemonAbilitiesResponse(PokemonAbilitiesNameResponse("running"))),
            pokemonSpecies = DUMMY_POKEMON_SPECIAL_1
        )
        val result = customResponse.toExternalModel()

        Assert.assertEquals(
            "Charmander", result.pokemonName
        )
        Assert.assertEquals(
            "Predator", result.pokemonTypes.first()
        )
        Assert.assertEquals(
            "Running", result.pokemonAbilities.first()
        )

    }

    @Test
    fun `NetworkMapper toExternalModel test upperCase firstChar data`() {
        val customResponse = PokemonDetailResponse(
            pokemonId = 1,
            pokemonName = "Charmander",
            pokemonWeight = 1,
            pokemonHeight = 1,
            pokemonImage = DUMMY_POKEMON_SPRITE,
            pokemonStats = DUMMY_LIST_POKEMON_BASIC_STAT,
            pokemonTypes = listOf(PokemonTypesResponse(PokemonTypeSingleResponse(typeName = "Predator"))),
            pokemonAbilities = listOf(PokemonAbilitiesResponse(PokemonAbilitiesNameResponse("Running"))),
            pokemonSpecies = DUMMY_POKEMON_SPECIAL_1
        )
        val result = customResponse.toExternalModel()

        Assert.assertEquals(
            "Charmander", result.pokemonName
        )
        Assert.assertEquals(
            "Predator", result.pokemonTypes.first()
        )
        Assert.assertEquals(
            "Running", result.pokemonAbilities.first()
        )

    }

    @Test
    fun `NetworkMapper toExternalModel test with nullable data`() {
        //given & when
        val customResponse = PokemonDetailResponse(
            pokemonImage = null,
            pokemonStats = DUMMY_LIST_POKEMON_BASIC_STAT,
            pokemonTypes = DUMMY_LIST_POKEMON_TYPE,
            pokemonAbilities = DUMMY_LIST_POKEMON_ABILITY,
            pokemonSpecies = null,
            pokemonId = null,
            pokemonName = null,
            pokemonWeight = null,
            pokemonHeight = null
        )
        val result = customResponse.toExternalModel()

        //then
        Assert.assertEquals(
            0, result.pokemonId
        )
        Assert.assertEquals(
            0, result.pokemonHeight
        )
        Assert.assertEquals(
            0, result.pokemonWeight
        )
        Assert.assertEquals(
            "No data available", result.pokemonName
        )
        Assert.assertEquals(
            "No data available", result.pokemonImage
        )
        Assert.assertEquals(
            "No data available", result.pokemonSpeciesUrl
        )

    }
}