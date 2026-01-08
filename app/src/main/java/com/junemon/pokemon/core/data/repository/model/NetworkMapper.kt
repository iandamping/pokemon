package com.junemon.pokemon.core.data.repository.model

import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonAbilitiesResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonBasicStatsResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesEggGroupResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonTypesResponse
import com.junemon.pokemon.util.PokemonConstant.ONE_EGG_MONS
import com.junemon.pokemon.util.PokemonConstant.ONE_SKILL_MONS
import com.junemon.pokemon.util.PokemonConstant.ONE_TYPE_MONS
import java.util.Locale

fun PokemonDetailResponse.toExternalModel(): PokemonDetail = PokemonDetail(
    pokemonId = pokemonId ?: 0,
    pokemonWeight = pokemonWeight ?: 0,
    pokemonHeight = pokemonHeight ?: 0,
    pokemonName = pokemonName?.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(
                Locale.getDefault()
            )
        } else {
            it.toString()
        }
    } ?: "No data available",
    pokemonImage = pokemonImage?.sprites?.other?.image ?: "No data available",
    pokemonSmallImages = listOf(
        pokemonImage?.smallImage1,
        pokemonImage?.smallImage2,
        pokemonImage?.smallImage3,
        pokemonImage?.smallImage4
    ),
    pokemonStats = listOf(
        pokemonStats[0].toExternalModel(),
        pokemonStats[1].toExternalModel(),
        pokemonStats[2].toExternalModel(),
        pokemonStats[3].toExternalModel(),
        pokemonStats[4].toExternalModel(),
        pokemonStats[5].toExternalModel(),
    ),
    pokemonTypes = listOf(
        pokemonTypes[0].type.typeName.replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) {
                firstChar.titlecase(
                    Locale.getDefault()
                )
            } else {
                firstChar.toString()
            }
        },
        pokemonTypes.checkTypes(1, 1)
    ),
    pokemonAbilities = listOf(
        pokemonAbilities[0].abilities.abilityName.replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) {
                firstChar.titlecase(
                    Locale.getDefault()
                )
            } else {
                firstChar.toString()
            }
        },
        pokemonAbilities.checkAbilities(1, 1)
    ),
    pokemonSpeciesUrl = pokemonSpecies?.speciesUrl ?: "No data available"
)

fun PokemonBasicStatsResponse.toExternalModel(): PokemonStat = PokemonStat(
    baseStat,
    statName.name.replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(
                Locale.getDefault()
            )
        } else {
            firstChar.toString()
        }
    }
)

fun PokemonSpeciesDetailResponse.toExternalModel(): PokemonDetailSpecies = PokemonDetailSpecies(
    happiness = pokemonHappiness ?: 0,
    captureRate = pokemonCaptureRate ?: 0,
    color = pokemonColor?.pokemonColor ?: "No data available",
    eggGroup1 = pokemonEggGroup[0].eggName,
    eggGroup2 = pokemonEggGroup.checkEggGroups(1, 1),
    generation = pokemonGeneration?.pokemonGenerationLString ?: "No data available",
    growthRate = pokemonGrowthRate?.pokemonGrowthRate ?: "No data available",
    habitat = pokemonHabitat?.pokemonHabitat ?: "No data available",
    shape = pokemonShape?.pokemonShape ?: "No data available",
    pokemonEggGroup = pokemonEggGroup.first().eggName,
    pokemonEggGroupUrl = pokemonEggGroup.first().url,
    pokemonEvolutionUrl = pokemonEvolution?.url ?: "No data available"
)

fun List<PokemonTypesResponse>.checkTypes(size: Int, position: Int): String =
    if (this.size > size) {
        this[position].type.typeName.replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) {
                firstChar.titlecase(
                    Locale.getDefault()
                )
            } else {
                firstChar.toString()
            }
        }
    } else {
        ONE_TYPE_MONS
    }

fun List<PokemonAbilitiesResponse>.checkAbilities(size: Int, position: Int): String =
    if (this.size > size) {
        this[position].abilities.abilityName.replaceFirstChar { firstChar ->
            if (firstChar.isLowerCase()) {
                firstChar.titlecase(
                    Locale.getDefault()
                )
            } else {
                firstChar.toString()
            }
        }
    } else {
        ONE_SKILL_MONS
    }

fun List<PokemonSpeciesEggGroupResponse>.checkEggGroups(size: Int, position: Int): String =
    if (this.size > size) {
        this[position].eggName
    } else {
        ONE_EGG_MONS
    }
