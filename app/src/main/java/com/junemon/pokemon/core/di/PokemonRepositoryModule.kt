package com.junemon.pokemon.core.di

import com.junemon.pokemon.core.data.repository.PokemonRepository
import com.junemon.pokemon.core.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PokemonRepositoryModule {

    @Binds
    fun bindsPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}