package com.junemon.pokemon.core.di

import com.junemon.pokemon.util.pokemonColorExtractor.PokemonColorExtractor
import com.junemon.pokemon.util.pokemonColorExtractor.PokemonColorExtractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PokemonColorExtractorModule {

    @Binds
    fun bindsPokemonColorExtractor(impl: PokemonColorExtractorImpl): PokemonColorExtractor
}
