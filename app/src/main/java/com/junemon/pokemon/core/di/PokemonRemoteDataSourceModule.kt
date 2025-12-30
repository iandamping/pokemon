package com.junemon.pokemon.core.di

import com.junemon.pokemon.core.data.dataSource.remote.PokemonRemoteDataSource
import com.junemon.pokemon.core.data.dataSource.remote.PokemonRemoteDataSourceImpl
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelper
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PokemonRemoteDataSourceModule {

    @Binds
    @Singleton
    fun bindsRetrofitHelper(impl: RetrofitHelperImpl): RetrofitHelper

    @Binds
    @Singleton
    fun bindsPokemonRemoteDataSource(impl: PokemonRemoteDataSourceImpl): PokemonRemoteDataSource
}
