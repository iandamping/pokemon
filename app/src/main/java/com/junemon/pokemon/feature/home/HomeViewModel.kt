package com.junemon.pokemon.feature.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Image
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.PokemonRepository
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.util.pokemonColorExtractor.PokemonColorExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val colorExtractor: PokemonColorExtractor
) : ViewModel() {

    val pokemonDetails: StateFlow<DomainResult<List<PokemonDetail>>> =
        repository.getPokemon().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DomainResult.Loading
        )

    private val _pokemonColors = MutableStateFlow<Map<Int, Color>>(emptyMap())
    val pokemonColors = _pokemonColors.asStateFlow()

    init {
        getPokemon()
    }

    private fun getPokemon() {
        viewModelScope.launch {
            repository.refreshPokemon()
        }
    }

    fun updatePokemonColor(pokemonId: Int, image: Image) {
        viewModelScope.launch {
            val color = colorExtractor.extractColorFromImage(
                pokemonId = pokemonId.toString(),
                image = image
            )
            _pokemonColors.update { currentMap ->
                currentMap + (pokemonId to color)
            }
        }
    }
}
