package com.junemon.pokemon.feature.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.model.PokemonDetail

@Composable
fun DetailRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val pokemonDetail by detailViewModel.pokemonDetail.collectAsStateWithLifecycle()
    val pokemonCharacteristic by detailViewModel.pokemonCharacteristic.collectAsStateWithLifecycle()
    val pokemonAreas by detailViewModel.pokemonAreas.collectAsStateWithLifecycle()
    val pokemonDetailArea by detailViewModel.pokemonDetailArea.collectAsStateWithLifecycle()
    val pokemonColors by detailViewModel.pokemonColors.collectAsStateWithLifecycle()

    when (val pokemonData = pokemonDetail) {
        is DomainResult.Data<PokemonDetail> -> {
            DetailScreen(
                pokemonData = pokemonData.data,
                dynamicCardColor = pokemonColors,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                modifier = modifier.padding(8.dp),
                onExtractColorFromImageWithId = { pokemonId, pokemonImage ->
                    detailViewModel.updatePokemonColor(pokemonId, pokemonImage)
                }
            )
        }

        is DomainResult.Error -> Text(pokemonData.message)
        DomainResult.Loading -> Text("Loading")
    }
}
