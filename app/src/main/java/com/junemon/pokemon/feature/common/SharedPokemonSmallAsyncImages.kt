package com.junemon.pokemon.feature.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Precision
import coil3.size.Scale
import com.junemon.pokemon.R

@Composable
fun SharedPokemonSmallAsyncImages(images: List<String?>, modifier: Modifier = Modifier) {
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
