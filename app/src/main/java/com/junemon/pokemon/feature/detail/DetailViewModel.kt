package com.junemon.pokemon.feature.detail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import coil3.Image
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.PokemonRepository
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonDetailSpecies
import com.junemon.pokemon.navigation.NavigationScreen
import com.junemon.pokemon.util.pokemonColorExtractor.PokemonColorExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val colorExtractor: PokemonColorExtractor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonIdRoute =
        savedStateHandle.toRoute<NavigationScreen.DetailScreen>()

    private val _pokemonId = MutableStateFlow(pokemonIdRoute.pokemonId)
    val pokemonId: StateFlow<Int> = _pokemonId.asStateFlow()

    private val _pokemonColors = MutableStateFlow<Map<Int, Color>>(emptyMap())
    val pokemonColors = _pokemonColors.asStateFlow()

    val pokemonDetail: StateFlow<DomainResult<PokemonDetail>> = pokemonId
        .transformLatest { id ->
            emit(repository.getPokemonById(id))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DomainResult.Loading
        )

    val pokemonCharacteristic: StateFlow<DomainResult<String>> = pokemonId
        .transformLatest { id ->
            emit(repository.getDetailPokemonCharacteristic(id))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DomainResult.Loading
        )

    val pokemonAreas: StateFlow<DomainResult<List<String>>> = pokemonId
        .transformLatest { id ->
            emit(repository.getPokemonLocationAreas(id))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DomainResult.Loading
        )

    val pokemonDetailArea: StateFlow<DomainResult<PokemonDetailSpecies>> = pokemonId
        .transformLatest { id ->
            emit(repository.getDetailSpeciesPokemon(id))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DomainResult.Loading
        )

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
