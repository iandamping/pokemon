package com.junemon.pokemon.core.data.dataSource.local.sharedPref

import kotlinx.coroutines.flow.Flow

interface DataStoreHelper {

    suspend fun saveLongInDataStore(value: Long)

    fun getLongInDataStore(): Flow<Long>
}
