package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {

    fun getDBCount(): Flow<Int>

    fun loadPokemon(): Flow<List<PokemonEntity>>

    suspend fun insertPokemon(data: List<PokemonEntity>)

    suspend fun deletePokemonData()

    suspend fun setLastUpdate(time: Long)

    fun getLastUpdate(): Flow<Long>
}
