package com.junemon.pokemon.core.di

import com.junemon.pokemon.core.data.dataSource.local.sharedPref.DataStoreHelper
import com.junemon.pokemon.core.data.dataSource.local.sharedPref.DataStoreHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Ian Damping on 03,January,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataStoreHelperModule {

    @Binds
    fun bindDataStoreHelper(dataStoreHelper: DataStoreHelperImpl): DataStoreHelper
}