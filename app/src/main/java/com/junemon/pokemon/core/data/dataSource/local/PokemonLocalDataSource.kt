package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {

    fun getDBCount(): Flow<Int>

    fun load(): Flow<List<PokemonEntity>>

    suspend fun insert(data: List<PokemonEntity>)

    suspend fun deleteAllData()

    suspend fun setLastUpdate(time: Long)

    fun getLastUpdate(): Flow<Long>
}