package com.junemon.pokemon.core.data.repository

import com.junemon.pokemon.core.data.dataSource.local.PokemonLocalDataSource
import com.junemon.pokemon.core.data.dataSource.remote.PokemonRemoteDataSource
import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.DEFAULT_ERROR
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.EMPTY_DATA
import com.junemon.pokemon.core.data.repository.model.PokemonDetail
import com.junemon.pokemon.core.data.repository.model.PokemonDetailSpecies
import com.junemon.pokemon.core.data.repository.model.toEntity
import com.junemon.pokemon.core.data.repository.model.toExternalModel
import com.junemon.pokemon.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.hours

@Suppress("TooGenericExceptionCaught")
class PokemonRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteData: PokemonRemoteDataSource,
    private val localData: PokemonLocalDataSource
) :
    PokemonRepository {

    private val databaseTimeout = 24.hours.inWholeMilliseconds

    override fun getPokemon(): Flow<DomainResult<List<PokemonDetail>>> {
        return localData.loadPokemon().map { entities ->
            if (entities.isEmpty()) {
                DomainResult.Loading
            } else {
                DomainResult.Data(entities.map { it.toExternalModel() })
            }
        }.catch {
            emit(DomainResult.Error("Local Database Error"))
        }
    }

    private suspend fun isLocalDataExpired(): Boolean {
        val count = localData.getDBCount().first()
        val lastSync = localData.getLastUpdate().first()

        return count == 0 || (System.currentTimeMillis() - lastSync) > databaseTimeout
    }

    override suspend fun refreshPokemon() {
        if (isLocalDataExpired()) {
            withContext(ioDispatcher) {
                val remoteResult = remoteData.getPokemon()
                if (remoteResult is DataSourceResult.Data) {
                    val data = withContext(ioDispatcher) {
                        remoteResult.data.map { singleItem ->
                            async {
                                remoteData.getDetailPokemon(singleItem.pokemonUrl)
                            }
                        }.awaitAll()
                    }

                    if (data.isEmpty()) {
                        DomainResult.Error(EMPTY_DATA)
                    } else {
                        localData.setLastUpdate(System.currentTimeMillis())
                        localData.insertPokemon(data.map { remoteData -> remoteData.toEntity() })
                    }
                }
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

                is DataSourceResult.Data -> DomainResult.Data(getPokemonFromRemote.data.toExternalModel())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> DomainResult.Error("Waktu koneksi habis (Timeout)")
                is IOException -> DomainResult.Error("Cek koneksi internet kamu")
                else -> DomainResult.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
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
                            .toExternalModel()
                    }
                }.awaitAll()
            }

            if (getPokemonFromRemote.isNotEmpty()) {
                DomainResult.Data(getPokemonFromRemote)
            } else {
                DomainResult.Error(EMPTY_DATA)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> DomainResult.Error("Waktu koneksi habis (Timeout)")
                is IOException -> DomainResult.Error("Cek koneksi internet kamu")
                else -> DomainResult.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun getDetailSpeciesPokemon(id: Int): DomainResult<PokemonDetailSpecies> {
        return when (val result = remoteData.getDetailSpeciesPokemon(id)) {
            is DataSourceResult.Data -> {
                DomainResult.Data(result.data.toExternalModel())
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
                DomainResult.Data(result.data.toExternalModel())
            }

            is DataSourceResult.Error -> {
                DomainResult.Error("$DEFAULT_ERROR ${result.message}")
            }
        }
    }
}
