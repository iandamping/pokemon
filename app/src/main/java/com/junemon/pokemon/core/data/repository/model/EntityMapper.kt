package com.junemon.pokemon.core.data.repository.model

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import java.util.Locale

fun PokemonDetailResponse.toEntity(): PokemonEntity = PokemonEntity(
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
    pokemonStatName0 = pokemonStats[0].toExternalModel().name,
    pokemonStatName1 = pokemonStats[1].toExternalModel().name,
    pokemonStatName2 = pokemonStats[2].toExternalModel().name,
    pokemonStatName3 = pokemonStats[3].toExternalModel().name,
    pokemonStatName4 = pokemonStats[4].toExternalModel().name,
    pokemonStatName5 = pokemonStats[5].toExternalModel().name,
    pokemonStatPoint0 = pokemonStats[0].toExternalModel().point,
    pokemonStatPoint1 = pokemonStats[1].toExternalModel().point,
    pokemonStatPoint2 = pokemonStats[2].toExternalModel().point,
    pokemonStatPoint3 = pokemonStats[3].toExternalModel().point,
    pokemonStatPoint4 = pokemonStats[4].toExternalModel().point,
    pokemonStatPoint5 = pokemonStats[5].toExternalModel().point,
    pokemonType0 = pokemonTypes[0].type.typeName.replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(
                Locale.getDefault()
            )
        } else {
            firstChar.toString()
        }
    },
    pokemonType1 = pokemonTypes.checkTypes(1, 1),
    pokemonAbility1 = pokemonAbilities[0].abilities.abilityName.replaceFirstChar { firstChar ->
        if (firstChar.isLowerCase()) {
            firstChar.titlecase(
                Locale.getDefault()
            )
        } else {
            firstChar.toString()
        }
    },
    pokemonAbility2 = pokemonAbilities.checkAbilities(1, 1),
    pokemonSpeciesUrl = pokemonSpecies?.speciesUrl ?: "No data available"
)

fun PokemonEntity.toExternalModel(): PokemonDetail = PokemonDetail(
    pokemonId = pokemonId,
    pokemonWeight = pokemonWeight,
    pokemonHeight = pokemonHeight,
    pokemonName = pokemonName,
    pokemonImage = pokemonImage,
    pokemonSmallImages = listOf(
        pokemonSmallImage1,
        pokemonSmallImage2,
        pokemonSmallImage3,
        pokemonSmallImage4,
    ),
    pokemonStats = listOf(
        PokemonStat(pokemonStatPoint0, pokemonStatName0),
        PokemonStat(pokemonStatPoint1, pokemonStatName1),
        PokemonStat(pokemonStatPoint2, pokemonStatName2),
        PokemonStat(pokemonStatPoint3, pokemonStatName3),
        PokemonStat(pokemonStatPoint4, pokemonStatName4),
        PokemonStat(pokemonStatPoint5, pokemonStatName5),
    ),
    pokemonTypes = listOf(
        pokemonType0,
        pokemonType1
    ),
    pokemonAbilities = listOf(
        pokemonAbility1,
        pokemonAbility2
    ),
    pokemonSpeciesUrl = pokemonSpeciesUrl
)
