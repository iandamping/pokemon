package com.junemon.pokemon.core.data.dataSource.local.sharedPref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelperImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    DataStoreHelper {

    private val lastUpdateName = "last update data"
    private val lastUpdateKey = longPreferencesKey(lastUpdateName)

    override suspend fun saveLongInDataStore(
        value: Long
    ) {
        dataStore.edit { preferences ->
            preferences[lastUpdateKey] = value
        }
    }

    override fun getLongInDataStore(): Flow<Long> {
        return dataStore.data.map { preferences -> preferences[lastUpdateKey] ?: 0 }
    }
}
