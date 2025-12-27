package com.junemon.pokemon.core.data.dataSource.remote.retrofit

import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonAreasResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonCharacteristicResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonEggGroupResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonEvolutionResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonMainResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface PokemonApi {

    @GET(NetworkConstant.GET_POKEMON)
    suspend fun getMainPokemon(): Response<PokemonMainResponse>

    @GET
    suspend fun getPokemon(@Url url: String): PokemonDetailResponse

    @GET("${NetworkConstant.GET_POKEMON}/{name}")
    suspend fun getPokemonDirectByName(@Path("name") name: String): PokemonDetailResponse

    @GET
    suspend fun getPokemonEggGroup(@Url url: String): PokemonEggGroupResponse

    @GET
    suspend fun getPokemonEvolution(@Url url: String): PokemonEvolutionResponse

    @GET("${NetworkConstant.GET_POKEMON_CHARACTERISTIC}/{id}")
    suspend fun getPokemonCharacteristic(@Path("id") id: Int): Response<PokemonCharacteristicResponse>

    @GET("${NetworkConstant.GET_POKEMON}/{id}/${NetworkConstant.GET_POKEMON_AREAS}")
    suspend fun getPokemonLocationAreas(@Path("id") id: Int): Response<List<PokemonAreasResponse>>

    @GET("${NetworkConstant.GET_POKEMON}/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<PokemonDetailResponse>

    @GET("${NetworkConstant.GET_POKEMON}/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<PokemonDetailResponse>

    @GET("${NetworkConstant.GET_POKEMON_SPECIES}/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): Response<PokemonSpeciesDetailResponse>
}