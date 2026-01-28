package com.junemon.pokemon.feature.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.Image
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.feature.common.PokemonIndicatorColorsProvider.progressIndicatorColors
import com.junemon.pokemon.feature.common.SharedPokemonAsyncImage
import com.junemon.pokemon.feature.common.SharedPokemonStat
import com.junemon.pokemon.feature.common.SharedPokemonTitle
import com.junemon.pokemon.util.PokemonConstant.ONE_TYPE_MONS

@Composable
fun DetailScreen(
    pokemonData: PokemonDetail,
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
        colors = CardDefaults.cardColors(
            containerColor = dynamicCardColor[pokemonData.pokemonId]?.copy(alpha = 0.2f)
                ?.compositeOver(Color.White)
                ?: MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    .compositeOver(Color.White)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            val (titleRef, imageRef, divider) = createRefs()
            val (statRef, typeRef) = createRefs()

            SharedPokemonTitle(
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                pokemonName = pokemonData.pokemonName,
                pokemonStat = pokemonData.pokemonStats.first().name,
                pokemonStatPoint = pokemonData.pokemonStats.first().point
            )

            SharedPokemonAsyncImage(
                modifier = Modifier.constrainAs(imageRef) {
                    top.linkTo(titleRef.bottom, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(250.dp)
                },
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

            HorizontalDivider(
                Modifier.constrainAs(divider) {
                    top.linkTo(imageRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                thickness = 1.5.dp
            )

            Row(
                modifier = Modifier.constrainAs(typeRef) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pokemonData.pokemonTypes.forEach { type ->
                    if (type != ONE_TYPE_MONS) {
                        Row {
                            Text(type)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.constrainAs(statRef) {
                    top.linkTo(typeRef.bottom)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
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
    }
}
