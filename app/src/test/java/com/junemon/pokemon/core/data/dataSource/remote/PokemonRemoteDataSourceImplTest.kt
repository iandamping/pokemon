package com.junemon.pokemon.core.data.dataSource.remote

import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_LIST_POKEMON_AREA
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_CHARACTERISTIC
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_DETAIL
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_MAIN_RESPONSE
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_SPECIES_DETAIL
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_URL_POKEMON_RESULTS_1
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_URL_POKEMON_RESULTS_2
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_URL_POKEMON_RESULTS_3
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelper
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelperImpl
import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonAreasResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonCharacteristicResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonMainResponse
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonSpeciesDetailResponse
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.PokemonApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRemoteDataSourceImplTest {

    private val api: PokemonApi = mockk()
    private val retrofitHelper: RetrofitHelper = RetrofitHelperImpl(UnconfinedTestDispatcher())
    private lateinit var sut: PokemonRemoteDataSource

    @Before
    fun setUp() {
        sut = PokemonRemoteDataSourceImpl(retrofitHelper = retrofitHelper, api = api)
    }

    @Test
    fun `GetPokemon should return success`() = runTest {
        // given
        val response = Response.success(
            DUMMY_POKEMON_MAIN_RESPONSE
        )
        coEvery { api.getMainPokemon() } returns response
        // when
        val results = sut.getPokemon()

        // then
        coVerify(exactly = 1) { api.getMainPokemon() }
        Assert.assertEquals(
            DUMMY_POKEMON_MAIN_RESPONSE.pokemonResults,
            (results as DataSourceResult.Data).data
        )
        Assert.assertEquals(DUMMY_URL_POKEMON_RESULTS_1, results.data[0])
        Assert.assertEquals(DUMMY_URL_POKEMON_RESULTS_2, results.data[1])
        Assert.assertEquals(DUMMY_URL_POKEMON_RESULTS_3, results.data[2])
    }

    @Test
    fun `GetPokemon should return error because empty data`() = runTest {
        // given
        val response = Response.success(
            PokemonMainResponse(pokemonResults = emptyList())
        )
        coEvery { api.getMainPokemon() } returns response

        // when
        val results = sut.getPokemon()
        // then
        coVerify(exactly = 1) { api.getMainPokemon() }
        Assert.assertEquals(
            "No data available",
            (results as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemon should return error from server`() = runTest {
        // given
        val response = Response.error<PokemonMainResponse>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getMainPokemon() } returns response

        // when
        val results = sut.getPokemon()
        // then
        coVerify(exactly = 1) { api.getMainPokemon() }
        Assert.assertEquals(
            "Error 500 error",
            (results as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemon should return error from exception`() = runTest {
        // given
        coEvery { api.getMainPokemon() } throws IOException()

        // when
        val results = sut.getPokemon()
        // then
        coVerify(exactly = 1) { api.getMainPokemon() }
        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (results as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetDetailPokemonCharacteristic should return success`() = runTest {
        // given
        val response = Response.success(
            DUMMY_POKEMON_CHARACTERISTIC
        )
        coEvery { api.getPokemonCharacteristic(any()) } returns response
        // when
        val results = sut.getDetailPokemonCharacteristic(1)
        val position =
            DUMMY_POKEMON_CHARACTERISTIC.descriptions.indexOfFirst { it.language.name == "en" }
        val expectedData = DUMMY_POKEMON_CHARACTERISTIC.descriptions[position].description
        // then
        coVerify(exactly = 1) { api.getPokemonCharacteristic(any()) }
        Assert.assertEquals(
            expectedData,
            (results as DataSourceResult.Data).data
        )
    }

    @Test
    fun `GetDetailPokemonCharacteristic should return error from server`() = runTest {
        // given
        val response = Response.error<PokemonCharacteristicResponse>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getPokemonCharacteristic(any()) } returns response
        // when
        val results = sut.getDetailPokemonCharacteristic(1)

        // then
        coVerify(exactly = 1) { api.getPokemonCharacteristic(any()) }
        Assert.assertEquals(
            "Error 500 error",
            (results as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetDetailPokemonCharacteristic should return error from exception`() = runTest {
        // given
        coEvery { api.getPokemonCharacteristic(any()) } throws IOException()
        // when
        val results = sut.getDetailPokemonCharacteristic(1)

        // then
        coVerify(exactly = 1) { api.getPokemonCharacteristic(any()) }
        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (results as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonLocationAreas should return success`() = runTest {
        val response = Response.success(
            DUMMY_LIST_POKEMON_AREA
        )
        coEvery { api.getPokemonLocationAreas(any()) } returns response

        val result = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { api.getPokemonLocationAreas(any()) }
        Assert.assertEquals(
            DataSourceResult.Data(DUMMY_LIST_POKEMON_AREA.map { it.area.name }),
            result
        )
    }

    @Test
    fun `GetPokemonLocationAreas should return error because empty data`() = runTest {
        val response = Response.success(
            emptyList<PokemonAreasResponse>()
        )
        coEvery { api.getPokemonLocationAreas(any()) } returns response

        val result = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { api.getPokemonLocationAreas(any()) }

        Assert.assertEquals(
            DataSourceResult.Error("No data available"),
            result
        )
    }

    @Test
    fun `GetPokemonLocationAreas should return error from server`() = runTest {
        val response = Response.error<List<PokemonAreasResponse>>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getPokemonLocationAreas(any()) } returns response

        val result = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { api.getPokemonLocationAreas(any()) }

        Assert.assertEquals(
            "Error 500 error",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonLocationAreas should return error from exception`() = runTest {
        coEvery { api.getPokemonLocationAreas(any()) } throws IOException()

        val result = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { api.getPokemonLocationAreas(any()) }

        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonById should return success`() = runTest {
        // given
        val response = Response.success(
            DUMMY_POKEMON_DETAIL
        )
        coEvery { api.getPokemonById(any()) } returns response
        // when
        val result = sut.getPokemonById(1)
        // then
        coVerify(exactly = 1) { api.getPokemonById(any()) }
        Assert.assertEquals(
            DataSourceResult.Data(DUMMY_POKEMON_DETAIL),
            result
        )
    }

    @Test
    fun `GetPokemonById should return error from server`() = runTest {
        // given
        val response = Response.error<PokemonDetailResponse>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getPokemonById(any()) } returns response
        // when
        val result = sut.getPokemonById(1)
        // then
        coVerify(exactly = 1) { api.getPokemonById(any()) }
        Assert.assertEquals(
            "Error 500 error",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonById should return error from exception`() = runTest {
        // given
        coEvery { api.getPokemonById(any()) } throws IOException()
        // when
        val result = sut.getPokemonById(1)
        // then
        coVerify(exactly = 1) { api.getPokemonById(any()) }
        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonByName should return success`() = runTest {
        // given
        val response = Response.success(
            DUMMY_POKEMON_DETAIL
        )
        coEvery { api.getPokemonByName(any()) } returns response
        // when
        val result = sut.getPokemonByName("a")
        // then
        coVerify(exactly = 1) { api.getPokemonByName(any()) }
        Assert.assertEquals(
            DataSourceResult.Data(DUMMY_POKEMON_DETAIL),
            result
        )
    }

    @Test
    fun `GetPokemonByName should return error from server`() = runTest {
        // given
        val response = Response.error<PokemonDetailResponse>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getPokemonByName(any()) } returns response
        // when
        val result = sut.getPokemonByName("1")
        // then
        coVerify(exactly = 1) { api.getPokemonByName(any()) }
        Assert.assertEquals(
            "Error 500 error",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetPokemonByName should return error from exception`() = runTest {
        // given
        coEvery { api.getPokemonByName(any()) } throws IOException()
        // when
        val result = sut.getPokemonByName("1")
        // then
        coVerify(exactly = 1) { api.getPokemonByName(any()) }
        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetDetailSpeciesPokemon should return success`() = runTest {
        // given
        val response = Response.success(
            DUMMY_POKEMON_SPECIES_DETAIL
        )
        coEvery { api.getPokemonSpecies(any()) } returns response
        // when
        val result = sut.getDetailSpeciesPokemon(1)
        // then
        coVerify(exactly = 1) { api.getPokemonSpecies(any()) }
        Assert.assertEquals(
            DataSourceResult.Data(DUMMY_POKEMON_SPECIES_DETAIL),
            result
        )
    }

    @Test
    fun `GetDetailSpeciesPokemon should return error from server`() = runTest {
        // given
        val response = Response.error<PokemonSpeciesDetailResponse>(
            500,
            "error".toResponseBody()
        )
        coEvery { api.getPokemonSpecies(any()) } returns response
        // when
        val result = sut.getDetailSpeciesPokemon(1)
        // then
        coVerify(exactly = 1) { api.getPokemonSpecies(any()) }
        Assert.assertEquals(
            "Error 500 error",
            (result as DataSourceResult.Error).message
        )
    }

    @Test
    fun `GetDetailSpeciesPokemon should return error from exception`() = runTest {
        // given
        coEvery { api.getPokemonSpecies(any()) } throws IOException()
        // when
        val result = sut.getDetailSpeciesPokemon(1)
        // then
        coVerify(exactly = 1) { api.getPokemonSpecies(any()) }
        Assert.assertEquals(
            "Error null Cek koneksi internet kamu",
            (result as DataSourceResult.Error).message
        )
    }
}
