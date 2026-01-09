package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePokemonLocalDataSource : PokemonLocalDataSource {
    private val pokemonEntities: MutableStateFlow<List<PokemonEntity>> =
        MutableStateFlow(emptyList())
    private var currentTime: MutableStateFlow<Long> = MutableStateFlow(0L)
    override fun getDBCount(): Flow<Int> {
        return pokemonEntities.map { it.size }
    }

    override fun loadPokemon(): Flow<List<PokemonEntity>> {
        return pokemonEntities.asStateFlow()
    }

    override suspend fun insertPokemon(data: List<PokemonEntity>) {
        pokemonEntities.update {
            it + data
        }
    }

    override suspend fun deletePokemonData() {
        pokemonEntities.value = emptyList()
    }

    override suspend fun setLastUpdate(time: Long) {
        currentTime.update { it + time }
    }

    override fun getLastUpdate(): Flow<Long> {
        return currentTime
    }
}
