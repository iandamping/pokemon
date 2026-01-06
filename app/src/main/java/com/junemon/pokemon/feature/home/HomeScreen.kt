package com.junemon.pokemon.feature.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.ui.state.ApiStates
import com.junemon.pokemon.ui.state.UiState

@Composable
fun HomeScreen(uiState: UiState<List<PokemonDetail>>, modifier: Modifier = Modifier) {
    val context: Context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            HomeTitleSection {
                Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            }
        }
        when (uiState.apiState) {
            ApiStates.IDLE -> {
                item { Text("Idle") }
            }

            ApiStates.LOADING -> {
                item { Text("Loading") }
            }

            ApiStates.SUCCESS -> {
                items(uiState.data!!, key = { key -> key.pokemonId }) { pokemon ->
                    ItemPokemonScreen(data = pokemon) { }
                }
            }

            ApiStates.FAILED -> {
                item { Text(uiState.errorMessage) }
            }
        }
    }
}
