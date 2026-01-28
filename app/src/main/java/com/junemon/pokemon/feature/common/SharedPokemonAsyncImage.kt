package com.junemon.pokemon.feature.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.junemon.pokemon.R

@Composable
fun SharedPokemonAsyncImage(
    imageUrl: String,
    pokemonId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onFetchImage: (Image) -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        AsyncImage(
            modifier = modifier.sharedElement(
                sharedTransitionScope.rememberSharedContentState(key = "image-$pokemonId"),
                animatedVisibilityScope = animatedContentScope
            ),
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
}
