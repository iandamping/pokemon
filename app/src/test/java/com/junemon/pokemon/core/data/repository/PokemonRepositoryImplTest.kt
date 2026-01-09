package com.junemon.pokemon.core.data.repository

import app.cash.turbine.test
import com.junemon.pokemon.core.data.dataSource.local.FakePokemonLocalDataSource
import com.junemon.pokemon.core.data.dataSource.local.PokemonLocalDataSource
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_DETAIL
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_MAIN_RESPONSE
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_SPECIES_DETAIL
import com.junemon.pokemon.core.data.dataSource.remote.PokemonRemoteDataSource
import com.junemon.pokemon.core.data.dataSource.remote.response.DataSourceResult
import com.junemon.pokemon.core.data.dataSource.remote.response.ItemPokemonEggResponse
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.DEFAULT_ERROR
import com.junemon.pokemon.core.data.dataSource.remote.retrofit.NetworkConstant.EMPTY_DATA
import com.junemon.pokemon.core.data.repository.model.toEntity
import com.junemon.pokemon.core.data.repository.model.toExternalModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {

    private val remoteData: PokemonRemoteDataSource = mockk()
    private val localData: PokemonLocalDataSource = FakePokemonLocalDataSource()
    private lateinit var sut: PokemonRepository

    @Before
    fun setUp() {
        sut =
            PokemonRepositoryImpl(ioDispatcher = UnconfinedTestDispatcher(), remoteData, localData)
    }

    @Test
    fun `GetPokemon should return DomainResult Loading because local data is empty`() = runTest {
        val results = sut.getPokemon()

        results.test {
            Assert.assertEquals(DomainResult.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GetPokemon should return DomainResult Data because local data is exist`() = runTest {
        localData.insertPokemon(listOf(DUMMY_POKEMON_DETAIL.toEntity()))
        val results = sut.getPokemon()

        results.test {
            Assert.assertEquals(
                listOf(DUMMY_POKEMON_DETAIL.toExternalModel()),
                (awaitItem() as DomainResult.Data).data
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshPokemon should trigger insert data in DB`() = runTest {
        coEvery { remoteData.getPokemon() } returns DataSourceResult.Data(
            DUMMY_POKEMON_MAIN_RESPONSE.pokemonResults
        )
        coEvery { remoteData.getDetailPokemon(any()) } returns DUMMY_POKEMON_DETAIL

        sut.refreshPokemon()

        coVerify(exactly = 1) { remoteData.getPokemon() }
        coVerify(exactly = 6) { remoteData.getDetailPokemon(any()) }

        localData.loadPokemon().test {
            Assert.assertEquals(
                DUMMY_POKEMON_DETAIL.toEntity(),
                awaitItem().first()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshPokemon should not trigger insert data in DB because empty data from remote`() =
        runTest {
            coEvery { remoteData.getPokemon() } returns DataSourceResult.Data(emptyList())

            sut.refreshPokemon()

            coVerify(exactly = 1) { remoteData.getPokemon() }
            coVerify(exactly = 0) { remoteData.getDetailPokemon(any()) }

            localData.getDBCount().test {
                Assert.assertEquals(
                    0,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `refreshPokemon should not trigger insert data in DB`() = runTest {
        localData.setLastUpdate(System.currentTimeMillis())
        localData.insertPokemon(listOf(DUMMY_POKEMON_DETAIL.toEntity()))

        sut.refreshPokemon()

        coVerify(exactly = 0) { remoteData.getPokemon() }
        coVerify(exactly = 0) { remoteData.getDetailPokemon(any()) }

        localData.loadPokemon().test {
            Assert.assertEquals(
                listOf(DUMMY_POKEMON_DETAIL.toEntity()),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GetEvolvingPokemon should return success`() = runTest {
        coEvery {
            remoteData.getPokemonEvolution(any())
        } returns DummyPokemon.DUMMY_POKEMON_EVOLUTION.evolutionChain.evolveTo.first().evolvingPokemonSpecies
        coEvery { remoteData.getPokemonByName(any()) } returns DataSourceResult.Data(
            DUMMY_POKEMON_DETAIL
        )

        val results = sut.getEvolvingPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEvolution(any()) }
        coVerify(exactly = 1) { remoteData.getPokemonByName(any()) }

        Assert.assertEquals(1, (results as DomainResult.Data).data.pokemonId)
        Assert.assertEquals(1, results.data.pokemonHeight)
        Assert.assertEquals(1, results.data.pokemonWeight)
        Assert.assertEquals("A", results.data.pokemonName)
    }

    @Test
    fun `GetEvolvingPokemon should return error from exception`() = runTest {
        coEvery { remoteData.getPokemonEvolution(any()) } throws IOException()

        val results = sut.getEvolvingPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEvolution("") }
        coVerify(exactly = 0) { remoteData.getPokemonByName(any()) }

        Assert.assertEquals(
            "Cek koneksi internet kamu",
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `GetSimilarEggGroupPokemon should return success`() = runTest {
        coEvery { remoteData.getPokemonEggGroup(any()) } returns DummyPokemon.DUMMY_POKEMON_EGG.eggGroupSpecies
        coEvery { remoteData.getDetailPokemonDirectByName(any()) } returns DUMMY_POKEMON_DETAIL

        val results = sut.getSimilarEggGroupPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEggGroup(any()) }
        coVerify(exactly = 3) { remoteData.getDetailPokemonDirectByName(any()) }

        Assert.assertEquals(1, (results as DomainResult.Data).data.first().pokemonId)
        Assert.assertEquals(1, results.data.first().pokemonHeight)
        Assert.assertEquals(1, results.data.first().pokemonWeight)
        Assert.assertEquals("A", results.data.first().pokemonName)
    }

    @Test
    fun `GetSimilarEggGroupPokemon should return error from empty data`() = runTest {
        coEvery { remoteData.getPokemonEggGroup(any()) } returns emptyList<ItemPokemonEggResponse>()
        coEvery { remoteData.getDetailPokemonDirectByName(any()) } returns DUMMY_POKEMON_DETAIL

        val results = sut.getSimilarEggGroupPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEggGroup(any()) }
        coVerify(exactly = 0) { remoteData.getDetailPokemonDirectByName(any()) }

        Assert.assertEquals(
            EMPTY_DATA,
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `GetSimilarEggGroupPokemon should return error from exception`() = runTest {
        coEvery { remoteData.getPokemonEggGroup(any()) } throws IOException()

        val results = sut.getSimilarEggGroupPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEggGroup(any()) }

        Assert.assertEquals(
            "Cek koneksi internet kamu",
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `GetSimilarEggGroupPokemon should return error from exception 2`() = runTest {
        coEvery { remoteData.getPokemonEggGroup(any()) } returns DummyPokemon.DUMMY_POKEMON_EGG.eggGroupSpecies
        coEvery { remoteData.getDetailPokemonDirectByName(any()) } throws IOException()

        val results = sut.getSimilarEggGroupPokemon("")

        coVerify(exactly = 1) { remoteData.getPokemonEggGroup(any()) }

        Assert.assertEquals(
            "Cek koneksi internet kamu",
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `GetDetailSpeciesPokemon should return success`() = runTest {
        coEvery { remoteData.getDetailSpeciesPokemon(any()) } returns DataSourceResult.Data(
            DUMMY_POKEMON_SPECIES_DETAIL
        )

        val results = sut.getDetailSpeciesPokemon(1)

        coVerify(exactly = 1) { remoteData.getDetailSpeciesPokemon(any()) }

        Assert.assertEquals(
            DUMMY_POKEMON_SPECIES_DETAIL.toExternalModel().captureRate,
            (results as DomainResult.Data).data.captureRate
        )
        Assert.assertEquals(
            DUMMY_POKEMON_SPECIES_DETAIL.toExternalModel().color,
            results.data.color
        )
        Assert.assertEquals(
            DUMMY_POKEMON_SPECIES_DETAIL.toExternalModel().eggGroup1,
            results.data.eggGroup1
        )
        Assert.assertEquals(
            DUMMY_POKEMON_SPECIES_DETAIL.toExternalModel().eggGroup2,
            results.data.eggGroup2
        )
    }

    @Test
    fun `GetDetailSpeciesPokemon should return error`() = runTest {
        coEvery { remoteData.getDetailSpeciesPokemon(any()) } returns DataSourceResult.Error("error")

        val results = sut.getDetailSpeciesPokemon(1)

        coVerify(exactly = 1) { remoteData.getDetailSpeciesPokemon(any()) }

        Assert.assertEquals(
            "$DEFAULT_ERROR error",
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `getPokemonLocationAreas should return success`() = runTest {
        coEvery { remoteData.getPokemonLocationAreas(any()) } returns DataSourceResult.Data(
            listOf("a", "a", "a")
        )

        val results = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { remoteData.getPokemonLocationAreas(any()) }

        Assert.assertEquals(listOf("a", "a", "a"), (results as DomainResult.Data).data)
    }

    @Test
    fun `GetPokemonLocationAreas should return error`() = runTest {
        coEvery { remoteData.getPokemonLocationAreas(any()) } returns DataSourceResult.Error("error")

        val results = sut.getPokemonLocationAreas(1)

        coVerify(exactly = 1) { remoteData.getPokemonLocationAreas(any()) }

        Assert.assertEquals(
            "$DEFAULT_ERROR error",
            (results as DomainResult.Error).message
        )
    }

    @Test
    fun `getPokemonById should return success`() = runTest {
        coEvery { remoteData.getPokemonById(any()) } returns DataSourceResult.Data(
            DUMMY_POKEMON_DETAIL
        )

        val results = sut.getPokemonById(1)

        coVerify(exactly = 1) { remoteData.getPokemonById(any()) }

        Assert.assertEquals(
            DUMMY_POKEMON_DETAIL.toExternalModel(),
            (results as DomainResult.Data).data
        )
        Assert.assertEquals(
            DUMMY_POKEMON_DETAIL.toExternalModel().pokemonId,
            results.data.pokemonId
        )
        Assert.assertEquals(
            DUMMY_POKEMON_DETAIL.toExternalModel().pokemonWeight,
            results.data.pokemonWeight
        )
        Assert.assertEquals(
            DUMMY_POKEMON_DETAIL.toExternalModel().pokemonHeight,
            results.data.pokemonHeight
        )
        Assert.assertEquals(
            DUMMY_POKEMON_DETAIL.toExternalModel().pokemonName,
            results.data.pokemonName
        )
    }

    @Test
    fun `getPokemonById should return error`() = runTest {
        coEvery { remoteData.getPokemonById(any()) } returns DataSourceResult.Error("error")

        val results = sut.getPokemonById(1)

        coVerify(exactly = 1) { remoteData.getPokemonById(any()) }

        Assert.assertEquals(
            "$DEFAULT_ERROR error",
            (results as DomainResult.Error).message
        )
    }
}
