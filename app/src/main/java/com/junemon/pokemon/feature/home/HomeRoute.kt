package com.junemon.pokemon.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val pokemonDetails by homeViewModel.pokemonDetails.collectAsStateWithLifecycle()
    val pokemonColors by homeViewModel.pokemonColors.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier,
        uiState = pokemonDetails,
        dynamicCardColor = pokemonColors,
        onProcessImageWithId = { pokemonId, image ->
            homeViewModel.updatePokemonColor(pokemonId = pokemonId, image = image)
        }
    )
}
