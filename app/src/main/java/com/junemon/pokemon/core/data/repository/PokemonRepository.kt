package com.junemon.pokemon.core.data.repository

import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonDetailSpecies
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemon(): Flow<DomainResult<List<PokemonDetail>>>

    suspend fun refreshPokemon()

    suspend fun getEvolvingPokemon(url: String): DomainResult<PokemonDetail>

    suspend fun getSimilarEggGroupPokemon(url: String): DomainResult<List<PokemonDetail>>

    suspend fun getDetailSpeciesPokemon(id: Int): DomainResult<PokemonDetailSpecies>

    suspend fun getDetailPokemonCharacteristic(id: Int): DomainResult<String>

    suspend fun getPokemonLocationAreas(id: Int): DomainResult<List<String>>

    suspend fun getPokemonById(id: Int): DomainResult<PokemonDetail>
}
