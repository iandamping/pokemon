package com.junemon.pokemon.core.data.dataSource.remote.helper

import com.example.juaraandroid_pokemonapp.core.data.datasource.remote.RetrofitHelperImpl
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_MAIN_RESPONSE
import com.junemon.pokemon.core.data.dataSource.remote.response.PokemonMainResponse
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.PokemonApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RetrofitHelperImplTest {
    private val api: PokemonApi = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var sut: RetrofitHelper

    @Before
    fun setUp() {
        sut = RetrofitHelperImpl(ioDispatcher = testDispatcher)
    }

    @Test
    fun `SafeApiCalls should return Success`() = runTest {
        // given
        val response = Response.success(
            200,
            DUMMY_POKEMON_MAIN_RESPONSE
        )
        coEvery { api.getMainPokemon() } returns response
        // when
        val result = sut.safeApiCalls { api.getMainPokemon() }

        coVerify(exactly = 1) { api.getMainPokemon() }

        // then
        Assert.assertEquals(
            ApiResult.Success(DUMMY_POKEMON_MAIN_RESPONSE),
            result
        )
    }

    @Test
    fun `SafeApiCalls should return Error because of null body`() =
        runTest {
            // given
            val response = Response.success<PokemonMainResponse>(
                200,
                null
            )
            coEvery { api.getMainPokemon() } returns response

            // when
            val result = sut.safeApiCalls { api.getMainPokemon() }

            coVerify(exactly = 1) { api.getMainPokemon() }

            // then
            Assert.assertEquals(
                ApiResult.Error(
                    message = "Response body is empty",
                    code = 200
                ),
                result
            )
        }

    @Test
    fun `SafeApiCalls should return Error because of Exception`() =
        runTest {
            // given
            coEvery { api.getMainPokemon() } throws IOException()

            // when
            val result = sut.safeApiCalls { api.getMainPokemon() }

            coVerify(exactly = 1) { api.getMainPokemon() }

            // then
            Assert.assertEquals(ApiResult.Error("Cek koneksi internet kamu"), result)
        }
}
