package com.junemon.pokemon.core.di

import com.junemon.pokemon.core.data.dataSource.local.PokemonLocalDataSource
import com.junemon.pokemon.core.data.dataSource.local.PokemonLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PokemonLocalDataSourceModule {

    @Binds
    @Singleton
    fun providePokemonLocalDataSource(impl: PokemonLocalDataSourceImpl): PokemonLocalDataSource
}
