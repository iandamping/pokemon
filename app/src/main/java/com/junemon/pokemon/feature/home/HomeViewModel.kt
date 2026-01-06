package com.junemon.pokemon.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junemon.pokemon.core.data.repository.DomainResult
import com.junemon.pokemon.core.data.repository.PokemonRepository
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.ui.state.ApiStates
import com.junemon.pokemon.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private val _pokemonDetails: MutableStateFlow<UiState<List<PokemonDetail>>> = MutableStateFlow(
        UiState.initialize()
    )
    val pokemonDetails: StateFlow<UiState<List<PokemonDetail>>> get() = _pokemonDetails.asStateFlow()

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
}
