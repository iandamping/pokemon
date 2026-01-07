package com.junemon.pokemon.feature.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil3.Image
import coil3.toBitmap
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.PokemonRepository
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.ui.state.ApiStates
import com.junemon.pokemon.ui.state.UiState
import com.junemon.pokemon.util.PokemonColorCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private val _pokemonDetails: MutableStateFlow<UiState<List<PokemonDetail>>> = MutableStateFlow(
        UiState.initialize()
    )
    val pokemonDetails: StateFlow<UiState<List<PokemonDetail>>> get() = _pokemonDetails.asStateFlow()

    private val _pokemonColors = MutableStateFlow<Map<Int, Color>>(emptyMap())
    val pokemonColors = _pokemonColors.asStateFlow()

    init {
        getPokemon()
    }

    private fun getPokemon() {
        viewModelScope.launch {
            _pokemonDetails.update { uiState ->
                uiState.copy(apiState = ApiStates.LOADING)
            }

            when (val repositoryData = repository.getPokemon()) {
                is DomainResult.Data<List<PokemonDetail>> -> _pokemonDetails.update { uiState ->
                    uiState.copy(apiState = ApiStates.SUCCESS, data = repositoryData.data)
                }

                is DomainResult.Error -> _pokemonDetails.update { uiState ->
                    uiState.copy(apiState = ApiStates.FAILED, errorMessage = repositoryData.message)
                }
            }
        }
    }

    fun updatePokemonColor(pokemonId: Int, image: Image) {
        viewModelScope.launch {
            val color = extractColorFromImage(pokemonId = pokemonId.toString(), image = image)
            _pokemonColors.update { currentMap ->
                currentMap + (pokemonId to color)
            }
        }
    }

    private suspend fun extractColorFromImage(pokemonId: String, image: Image): Color =
        withContext(Dispatchers.IO) {
            // 1. Used cache color first from LruCache if exist
            PokemonColorCache.get(pokemonId)
                ?.let { cachedColor -> return@withContext cachedColor }
            // 2. if not exist cache, do heavy bitmap computation here
            val bitmap = image.toBitmap()
            val palette = Palette.from(bitmap).generate()

            val swatch =
                palette.vibrantSwatch ?: palette.lightVibrantSwatch ?: palette.mutedSwatch

            val extractedColor = swatch?.let { Color(it.rgb) } ?: Color.LightGray
            // 3. Save palette colors into cache for next usage
            PokemonColorCache.put(pokemonId, extractedColor)
            extractedColor
        }
}
