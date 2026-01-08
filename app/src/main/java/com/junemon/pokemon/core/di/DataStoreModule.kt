package com.junemon.pokemon.core.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ian Damping on 23,September,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val DATASTORE_INIT = "pokemon datastore"

    private val Context.dataStore by preferencesDataStore(
        name = DATASTORE_INIT
    )

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) = context.dataStore
}