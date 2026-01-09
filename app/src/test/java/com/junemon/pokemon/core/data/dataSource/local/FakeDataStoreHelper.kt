package com.junemon.pokemon.core.data.dataSource.local

import com.junemon.pokemon.core.data.dataSource.local.sharedPref.DataStoreHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeDataStoreHelper : DataStoreHelper {
    private var currentTime: MutableStateFlow<Long> = MutableStateFlow(0L)
    override suspend fun saveLongInDataStore(value: Long) {
        currentTime.update { it + value }
    }

    override fun getLongInDataStore(): Flow<Long> {
        return currentTime
    }
}
