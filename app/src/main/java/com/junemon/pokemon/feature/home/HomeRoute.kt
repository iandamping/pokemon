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
    HomeScreen(modifier = modifier, uiState = pokemonDetails)
}
