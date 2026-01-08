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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.Image
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.model.PokemonDetail

@Composable
fun HomeScreen(
    uiState: DomainResult<List<PokemonDetail>>,
    dynamicCardColor: Map<Int, Color>,
    onProcessImageWithId: (Int, Image) -> Unit,
    modifier: Modifier = Modifier
) {
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
        when (uiState) {
            is DomainResult.Data<List<PokemonDetail>> -> {
                items(uiState.data, key = { key -> key.pokemonId }) { pokemon ->
                    ItemPokemonScreen(
                        data = pokemon,
                        dynamicCardColor = dynamicCardColor,
                        onSelectedPokemon = {},
                        onProcessImageWithId = onProcessImageWithId
                    )
                }
            }

            is DomainResult.Error -> item { Text(uiState.message) }
            DomainResult.Loading -> item { Text("Loading") }
        }
    }
}
