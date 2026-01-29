package com.junemon.pokemon.feature.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.Image
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonDetailSpecies
import com.junemon.pokemon.feature.common.PokemonIndicatorColorsProvider.progressIndicatorColors
import com.junemon.pokemon.feature.common.SharedPokemonAsyncImage
import com.junemon.pokemon.feature.common.SharedPokemonSmallAsyncImages
import com.junemon.pokemon.feature.common.SharedPokemonStat
import com.junemon.pokemon.feature.common.SharedPokemonTitle
import com.junemon.pokemon.util.PokemonConstant.ONE_TYPE_MONS

@Composable
fun DetailScreen(
    pokemonData: PokemonDetail,
    pokemonCharacteristic: DomainResult<String>,
    pokemonSpecies: DomainResult<PokemonDetailSpecies>,
    dynamicCardColor: Map<Int, Color>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    onExtractColorFromImageWithId: (Int, Image) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = dynamicCardColor[pokemonData.pokemonId]?.copy(alpha = 0.2f)
                ?.compositeOver(Color.White)
                ?: MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    .compositeOver(Color.White)
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SharedPokemonTitle(
                modifier = Modifier.fillMaxWidth(),
                pokemonName = pokemonData.pokemonName,
                pokemonStat = pokemonData.pokemonStats.first().name,
                pokemonStatPoint = pokemonData.pokemonStats.first().point
            )

            SharedPokemonAsyncImage(
                modifier = Modifier
                    .size(250.dp)
                    .padding(vertical = 8.dp),
                imageUrl = pokemonData.pokemonImage,
                pokemonId = pokemonData.pokemonId,
                animatedContentScope = animatedContentScope,
                sharedTransitionScope = sharedTransitionScope,
                onFetchImage = { image ->
                    onExtractColorFromImageWithId(
                        pokemonData.pokemonId,
                        image
                    )
                }
            )

            SharedPokemonSmallAsyncImages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                images = pokemonData.pokemonSmallImages
            )

            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 1.5.dp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pokemonData.pokemonTypes.forEach { type ->
                    if (type != ONE_TYPE_MONS) {
                        Row {
                            SuggestionChip(onClick = {}, label = {
                                Text(type)
                            })
                        }
                    }
                }
            }

            PokemonStat(
                modifier = Modifier.fillMaxWidth(),
                pokemonData = pokemonData
            )

            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 1.5.dp
            )

            PokemonCharacteristic(
                modifier = Modifier.fillMaxWidth(),
                pokemonCharacter = pokemonCharacteristic
            )

            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 1.5.dp
            )

            PokemonMetaData(
                modifier = Modifier.fillMaxWidth(),
                pokemonSpecies = pokemonSpecies
            )
        }
    }
}

@Composable
fun PokemonStat(pokemonData: PokemonDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Base Stat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        pokemonData.pokemonStats.forEachIndexed { index, stat ->
            SharedPokemonStat(
                modifier = Modifier.fillMaxWidth(),
                statName = stat.name,
                statPoint = stat.point,
                selectedColor = progressIndicatorColors[index]
            )
        }
    }
}

@Composable
fun PokemonMetaData(
    pokemonSpecies: DomainResult<PokemonDetailSpecies>,
    modifier: Modifier = Modifier
) {
    when (val species = pokemonSpecies) {
        is DomainResult.Data<PokemonDetailSpecies> -> {
            val items = listOf(
                "Habitat" to species.data.habitat,
                "Group" to species.data.pokemonEggGroup,
                "Growth" to species.data.growthRate,
                "Capture Rate" to "${species.data.captureRate}%"
            )
            // Menggunakan Column dan Row untuk mensimulasikan Grid sederhana
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items.chunked(2).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowItems.forEach { (label, value) ->
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    value,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        is DomainResult.Error -> {
            Row(modifier = modifier) {
                Text("Species : Unknown")
            }
        }

        DomainResult.Loading -> Text("loading")
    }
}

@Composable
private fun PokemonCharacteristic(
    pokemonCharacter: DomainResult<String>,
    modifier: Modifier = Modifier
) {
    when (val char = pokemonCharacter) {
        is DomainResult.Data<String> -> {
            Row(modifier = modifier) {
                Text("Character : ${char.data}")
            }
        }

        is DomainResult.Error -> {
            Row(modifier = modifier) {
                Text("Character : Unknown")
            }
        }

        DomainResult.Loading -> Text("loading")
    }
}
