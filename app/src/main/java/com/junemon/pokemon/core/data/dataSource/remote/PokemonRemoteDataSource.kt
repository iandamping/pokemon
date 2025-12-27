package com.junemon.pokemon.core.data.dataSource.remote

import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.response.EvolvingPokemon
import com.junemon.pokemon.core.data.dataSource.remote.response.ItemPokemonEggResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonResultsResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesDetailResponse

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface PokemonRemoteDataSource {

    suspend fun getPokemon(): DataSourceResult<List<PokemonResultsResponse>>

    suspend fun getDetailPokemon(url: String): PokemonDetailResponse

    suspend fun getDetailPokemonDirectByName(name: String): PokemonDetailResponse

    suspend fun getPokemonEggGroup(url: String): List<ItemPokemonEggResponse>

    suspend fun getPokemonEvolution(url: String): EvolvingPokemon

    suspend fun getDetailPokemonCharacteristic(id: Int): DataSourceResult<String>

    suspend fun getPokemonLocationAreas(id: Int): DataSourceResult<List<String>>

    suspend fun getPokemonById(id: Int): DataSourceResult<PokemonDetailResponse>

    suspend fun getPokemonByName(name: String): DataSourceResult<PokemonDetailResponse>

    suspend fun getDetailSpeciesPokemon(id: Int): DataSourceResult<PokemonSpeciesDetailResponse>
}