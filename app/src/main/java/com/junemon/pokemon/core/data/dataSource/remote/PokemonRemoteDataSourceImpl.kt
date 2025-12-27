package com.junemon.pokemon.core.data.dataSource.remote

import com.junemon.pokemon.core.data.dataSource.remote.helper.ApiResult
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelper
import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.response.EvolvingPokemon
import com.junemon.pokemon.core.data.dataSource.remote.response.ItemPokemonEggResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonResultsResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.PokemonApi
import javax.inject.Inject

class PokemonRemoteDataSourceImpl @Inject constructor(
    retrofitHelper: RetrofitHelper,
    private val api: PokemonApi
) : PokemonRemoteDataSource, RetrofitHelper by retrofitHelper {
    override suspend fun getPokemon(): DataSourceResult<List<PokemonResultsResponse>> {
        return when (val response = safeApiCalls { api.getMainPokemon() }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> if (response.data.pokemonResults.isEmpty()) {
                DataSourceResult.Error("No data available")
            } else DataSourceResult.Data(response.data.pokemonResults)
        }
    }


    override suspend fun getDetailPokemon(url: String): PokemonDetailResponse {
        return api.getPokemon(url)
    }


    override suspend fun getDetailPokemonDirectByName(name: String): PokemonDetailResponse {
        return api.getPokemonDirectByName(name)
    }


    override suspend fun getPokemonEggGroup(url: String): List<ItemPokemonEggResponse> {
        return api.getPokemonEggGroup(url).eggGroupSpecies
    }


    override suspend fun getPokemonEvolution(url: String): EvolvingPokemon {
        return api.getPokemonEvolution(url).evolutionChain.evolveTo.first().evolvingPokemonSpecies
    }

    override suspend fun getDetailPokemonCharacteristic(id: Int): DataSourceResult<String> {
        return when (val response = safeApiCalls { api.getPokemonCharacteristic(id) }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> {
                val position = response.data.descriptions.indexOfFirst { it.language.name == "en" }
                DataSourceResult.Data(response.data.descriptions[position].description)
            }
        }
    }

    override suspend fun getPokemonLocationAreas(id: Int): DataSourceResult<List<String>> {
        return when (val response = safeApiCalls { api.getPokemonLocationAreas(id) }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> {
                if (response.data.isNotEmpty()) {
                    DataSourceResult.Data(response.data.map { it.area.name })
                } else DataSourceResult.Error("No data available")
            }
        }
    }

    override suspend fun getPokemonById(id: Int): DataSourceResult<PokemonDetailResponse> {
        return when (val response = safeApiCalls { api.getPokemonById(id) }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> DataSourceResult.Data(response.data)
        }
    }

    override suspend fun getPokemonByName(name: String): DataSourceResult<PokemonDetailResponse> {
        return when (val response = safeApiCalls { api.getPokemonByName(name) }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> DataSourceResult.Data(response.data)
        }
    }

    override suspend fun getDetailSpeciesPokemon(id: Int): DataSourceResult<PokemonSpeciesDetailResponse> {
        return when (val response = safeApiCalls { api.getPokemonSpecies(id) }) {
            is ApiResult.Error -> DataSourceResult.Error("Error ${response.code} ${response.message}")
            is ApiResult.Success -> DataSourceResult.Data(response.data)
        }
    }
}
