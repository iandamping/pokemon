package com.junemon.pokemon.core.data.dataSource.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ian Damping on 10,June,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Dao
interface PokemonDao {
    @Query("SELECT COUNT(*) FROM main_pokemon")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM main_pokemon")
    fun load(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<PokemonEntity>)

    @Query("DELETE FROM main_pokemon")
    suspend fun deleteAllData()
}
