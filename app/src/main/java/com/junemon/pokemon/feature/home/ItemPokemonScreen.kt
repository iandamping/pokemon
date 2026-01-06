package com.junemon.pokemon.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Precision
import coil3.size.Scale
import com.junemon.pokemon.R
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.util.PokemonConstant.ONE_TYPE_MONS
import timber.log.Timber

@Composable
fun ItemPokemonScreen(
    data: PokemonDetail,
    modifier: Modifier = Modifier,
    onSelectedPokemon: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onSelectedPokemon(data.pokemonId) },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
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

            AsyncImage(
                modifier = Modifier.constrainAs(imageRef) {
                    top.linkTo(titleRef.bottom, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)

                    width = Dimension.fillToConstraints
                    height = Dimension.value(150.dp)
                },
                model = ImageRequest.Builder(LocalContext.current).data(data.pokemonImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.ic_no_data),
                contentDescription = stringResource(R.string.pokemon_image)
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
                        Timber.e(type)
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
                data.pokemonStats.forEach { stat ->
                    ItemPokemonStat(
                        modifier = Modifier.fillMaxWidth(),
                        statName = stat.name,
                        statPoint = stat.point
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
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ItemPokemonStat(
    statName: String,
    statPoint: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = statPoint.toString(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ItemPokemonSmallImages(images: List<String?>, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        images.forEach { item ->
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(LocalContext.current).data(item)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .precision(Precision.EXACT)
                    .build(),
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.ic_no_data),
                contentDescription = stringResource(R.string.pokemon_image),
                contentScale = ContentScale.Crop
            )
        }
    }
}
