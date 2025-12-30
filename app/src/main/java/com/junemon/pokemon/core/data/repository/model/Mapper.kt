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

fun PokemonDetailResponse.mapToDetail(): PokemonDetail = PokemonDetail(
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
    pokemonSmallImage1 = pokemonImage?.smallImage1 ?: "No data available",
    pokemonSmallImage2 = pokemonImage?.smallImage2 ?: "No data available",
    pokemonSmallImage3 = pokemonImage?.smallImage3 ?: "No data available",
    pokemonSmallImage4 = pokemonImage?.smallImage4 ?: "No data available",
    pokemonStat0 = pokemonStats[0].mapToDetail(),
    pokemonStat1 = pokemonStats[1].mapToDetail(),
    pokemonStat2 = pokemonStats[2].mapToDetail(),
    pokemonStat3 = pokemonStats[3].mapToDetail(),
    pokemonStat4 = pokemonStats[4].mapToDetail(),
    pokemonStat5 = pokemonStats[5].mapToDetail(),
    pokemonType0 = pokemonTypes[0].type.typeName.replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(
                Locale.getDefault()
            )
        } else {
            firstChar.toString()
        }
    },
    pokemonType1 = pokemonTypes.checkTypeList(1, 1),
    pokemonAbility1 = pokemonAbilities[0].abilities.abilityName.replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(
                Locale.getDefault()
            )
        } else {
            firstChar.toString()
        }
    },
    pokemonAbility2 = pokemonAbilities.checkAbilitiesList(1, 1),
    pokemonSpeciesUrl = pokemonSpecies?.speciesUrl ?: "No data available"
)

fun PokemonBasicStatsResponse.mapToDetail(): PokemonStat = PokemonStat(
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

fun List<PokemonTypesResponse>.checkTypeList(size: Int, position: Int): String =
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

fun List<PokemonAbilitiesResponse>.checkAbilitiesList(size: Int, position: Int): String =
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

fun PokemonSpeciesDetailResponse.mapToSpeciesDetail(): PokemonDetailSpecies = PokemonDetailSpecies(
    happiness = pokemonHappiness ?: 0,
    captureRate = pokemonCaptureRate ?: 0,
    color = pokemonColor?.pokemonColor ?: "No data available",
    eggGroup1 = pokemonEggGroup[0].eggName,
    eggGroup2 = pokemonEggGroup.checkEggGroupList(1, 1),
    generation = pokemonGeneration?.pokemonGenerationLString ?: "No data available",
    growthRate = pokemonGrowthRate?.pokemonGrowthRate ?: "No data available",
    habitat = pokemonHabitat?.pokemonHabitat ?: "No data available",
    shape = pokemonShape?.pokemonShape ?: "No data available",
    pokemonEggGroup = pokemonEggGroup.first().eggName,
    pokemonEggGroupUrl = pokemonEggGroup.first().url,
    pokemonEvolutionUrl = pokemonEvolution?.url ?: "No data available"
)

fun List<PokemonSpeciesEggGroupResponse>.checkEggGroupList(size: Int, position: Int): String =
    if (this.size > size) {
        this[position].eggName
    } else {
        ONE_EGG_MONS
    }
