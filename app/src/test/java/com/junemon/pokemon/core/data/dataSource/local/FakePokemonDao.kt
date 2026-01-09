package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonDao
import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePokemonDao : PokemonDao {
    private val pokemonEntities: MutableStateFlow<List<PokemonEntity>> =
        MutableStateFlow(emptyList())

    override fun getCount(): Flow<Int> {
        return pokemonEntities.map { it.size }
    }

    override fun load(): Flow<List<PokemonEntity>> {
        return pokemonEntities.asStateFlow()
    }

    override suspend fun insert(data: List<PokemonEntity>) {
        pokemonEntities.update {
            it + data
        }
    }

    override suspend fun deleteAllData() {
        pokemonEntities.value = emptyList()
    }
}
