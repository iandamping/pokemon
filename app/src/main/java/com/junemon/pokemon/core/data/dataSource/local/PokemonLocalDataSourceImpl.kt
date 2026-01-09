package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.database.PokemonDao
import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import com.junemon.pokemon.core.data.dataSource.local.sharedPref.DataStoreHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonLocalDataSourceImpl @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val dataStoreHelper: DataStoreHelper
) :
    PokemonLocalDataSource {
    override fun getDBCount(): Flow<Int> {
        return pokemonDao.getCount()
    }

    override fun loadPokemon(): Flow<List<PokemonEntity>> {
        return pokemonDao.load()
    }

    override suspend fun insertPokemon(data: List<PokemonEntity>) {
        pokemonDao.insert(data)
    }

    override suspend fun deletePokemonData() {
        pokemonDao.deleteAllData()
    }

    override suspend fun setLastUpdate(time: Long) {
        dataStoreHelper.saveLongInDataStore(time)
    }

    override fun getLastUpdate(): Flow<Long> {
        return dataStoreHelper.getLongInDataStore()
    }
}
