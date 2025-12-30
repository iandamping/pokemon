package com.junemon.pokemon.core.data.repository

import com.junemon.pokemon.core.data.dataSource.remote.PokemonRemoteDataSource
import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.DEFAULT_ERROR
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.EMPTY_DATA
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonDetailSpecies
import com.junemon.pokemon.core.data.repository.model.mapToDetail
import com.junemon.pokemon.core.data.repository.model.mapToSpeciesDetail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteData: PokemonRemoteDataSource
) :
    PokemonRepository {
    override suspend fun getPokemon(): DomainResult<List<PokemonDetail>> {
        return when (val result = remoteData.getPokemon()) {
            is DataSourceResult.Data -> {
                try {
                    val data = withContext(ioDispatcher) {
                        result.data.map { singleItem ->
                            async {
                                remoteData.getDetailPokemon(singleItem.pokemonUrl).mapToDetail()
                            }
                        }.awaitAll()
                    }

                    if (data.isEmpty()) {
                        DomainResult.Error(EMPTY_DATA)
                    } else {
                        DomainResult.Data(data)
                    }
                } catch (e: Exception) {
                    DomainResult.Error(e.message ?: EMPTY_DATA)
                }
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }

    override suspend fun getEvolvingPokemon(url: String): DomainResult<PokemonDetail> {
        return try {
            val remoteResult = remoteData.getPokemonEvolution(url)
            when (val getPokemonFromRemote = remoteData.getPokemonByName(remoteResult.eggName)) {
                is DataSourceResult.Error -> DomainResult.Error(
                    getPokemonFromRemote.message
                )

                is DataSourceResult.Data -> DomainResult.Data(getPokemonFromRemote.data.mapToDetail())
            }
        } catch (e: Exception) {
            DomainResult.Error(e.message ?: EMPTY_DATA)
        }
    }

    override suspend fun getSimilarEggGroupPokemon(url: String): DomainResult<List<PokemonDetail>> {
        return try {
            val remoteResultName =
                remoteData.getPokemonEggGroup(url).shuffled().take(4).map { it.name }

            val getPokemonFromRemote = withContext(ioDispatcher) {
                remoteResultName.map { singleItem ->
                    async {
                        remoteData.getDetailPokemonDirectByName(singleItem)
                            .mapToDetail()
                    }
                }.awaitAll()
            }

            if (getPokemonFromRemote.isNotEmpty()) {
                DomainResult.Data(getPokemonFromRemote)
            } else {
                DomainResult.Error(EMPTY_DATA)
            }
        } catch (e: Exception) {
            DomainResult.Error(e.message ?: EMPTY_DATA)
        }
    }

    override suspend fun getDetailSpeciesPokemon(id: Int): DomainResult<PokemonDetailSpecies> {
        return when (val result = remoteData.getDetailSpeciesPokemon(id)) {
            is DataSourceResult.Data -> {
                DomainResult.Data(result.data.mapToSpeciesDetail())
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }

    override suspend fun getDetailPokemonCharacteristic(id: Int): DomainResult<String> {
        return when (val result = remoteData.getDetailPokemonCharacteristic(id)) {
            is DataSourceResult.Data -> {
                DomainResult.Data(result.data)
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }

    override suspend fun getPokemonLocationAreas(id: Int): DomainResult<List<String>> {
        return when (val result = remoteData.getPokemonLocationAreas(id)) {
            is DataSourceResult.Data -> {
                DomainResult.Data(result.data)
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }

    override suspend fun getPokemonById(id: Int): DomainResult<PokemonDetail> {
        return when (val result = remoteData.getPokemonById(id)) {
            is DataSourceResult.Data -> {
                DomainResult.Data(result.data.mapToDetail())
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }
}
