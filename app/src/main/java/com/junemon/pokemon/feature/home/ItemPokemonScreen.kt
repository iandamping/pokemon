package com.junemon.pokemon.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.junemon.pokemon.R
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.ui.theme.Pink40
import com.junemon.pokemon.ui.theme.Pink80
import com.junemon.pokemon.ui.theme.Purple40
import com.junemon.pokemon.ui.theme.Purple80
import com.junemon.pokemon.ui.theme.PurpleGrey40
import com.junemon.pokemon.ui.theme.PurpleGrey80
import com.junemon.pokemon.util.PokemonConstant.ONE_TYPE_MONS

@Composable
fun ItemPokemonScreen(
    data: PokemonDetail,
    dynamicCardColor: Map<Int, Color>,
    modifier: Modifier = Modifier,
    onProcessImageWithId: (Int, Image) -> Unit,
    onSelectedPokemon: (Int) -> Unit
) {
    val progressIndicatorColors: List<Color> =
        listOf(Purple80, PurpleGrey80, Pink80, Purple40, PurpleGrey40, Pink40)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onSelectedPokemon(data.pokemonId) },
        colors = CardDefaults.cardColors(
            containerColor = dynamicCardColor[data.pokemonId]?.copy(alpha = 0.2f)
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

            ItemPokemonTitle(
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                pokemonName = data.pokemonName,
                pokemonStat = data.pokemonStats.first().name,
                pokemonStatPoint = data.pokemonStats.first().point
            )

            ItemPokemonMainImages(
                modifier = Modifier.constrainAs(imageRef) {
                    top.linkTo(titleRef.bottom, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(150.dp)
                },
                imageUrl = data.pokemonImage,
                onFetchImage = { image ->
                    onProcessImageWithId(data.pokemonId, image)
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
                data.pokemonTypes.forEach { type ->
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
                data.pokemonStats.forEachIndexed { index, stat ->
                    ItemPokemonStat(
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

@Composable
private fun ItemPokemonTitle(
    pokemonName: String,
    pokemonStat: String,
    pokemonStatPoint: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = pokemonName,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "$pokemonStat $pokemonStatPoint",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black
            )
        )
    }
}

@Composable
private fun ItemPokemonStat(
    statName: String,
    statPoint: Int,
    selectedColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            modifier = Modifier.weight(0.35f),
            style = MaterialTheme.typography.labelMedium
        )

        LinearProgressIndicator(
            progress = {
                statPoint.toFloat() / 200F
            },
            modifier = Modifier
                .weight(0.65f)
                .height(8.dp)
                .clip(CircleShape),
            color = selectedColor,
            trackColor = selectedColor.copy(alpha = 0.2f)
        )
        Text(
            text = statPoint.toString(),
            modifier = Modifier.weight(0.1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun ItemPokemonMainImages(
    imageUrl: String,
    onFetchImage: (Image) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .crossfade(true)
            .allowHardware(false) // for palette library
            .build(),
        placeholder = painterResource(id = R.drawable.placeholder_image),
        error = painterResource(id = R.drawable.ic_no_data),
        contentDescription = stringResource(R.string.pokemon_image),
        onSuccess = { success ->
            // image for palette library
            onFetchImage(success.result.image)
        }
    )
}
