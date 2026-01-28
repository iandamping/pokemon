package com.junemon.pokemon.feature.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMoveToDetailScreen: (Int) -> Unit,
) {
    val pokemonDetails by homeViewModel.pokemonDetails.collectAsStateWithLifecycle()
    val pokemonColors by homeViewModel.pokemonColors.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        uiState = pokemonDetails,
        dynamicCardColor = pokemonColors,
        onExtractColorFromImageWithId = { pokemonId, image ->
            homeViewModel.updatePokemonColor(pokemonId = pokemonId, image = image)
        },
        onSelectedPokemon = onMoveToDetailScreen
    )
}
