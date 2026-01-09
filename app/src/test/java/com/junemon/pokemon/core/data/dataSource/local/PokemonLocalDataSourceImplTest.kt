package com.junemon.pokemon.core.data.dataSource.local

import app.cash.turbine.test
import com.junemon.pokemon.core.data.dataSource.local.database.PokemonDao
import com.junemon.pokemon.core.data.dataSource.local.database.PokemonEntity
import com.junemon.pokemon.core.data.dataSource.local.sharedPref.DataStoreHelper
import com.junemon.pokemon.core.data.dataSource.remote.DummyPokemon.DUMMY_POKEMON_DETAIL
import com.junemon.pokemon.core.data.repository.model.toEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PokemonLocalDataSourceImplTest {

    private val pokemonDao: PokemonDao = FakePokemonDao()
    private val dataStoreHelper: DataStoreHelper = FakeDataStoreHelper()
    private lateinit var sut: PokemonLocalDataSource

    @Before
    fun setup() {
        sut = PokemonLocalDataSourceImpl(pokemonDao = pokemonDao, dataStoreHelper = dataStoreHelper)
    }

    @Test
    fun `test getDBCount should return empty`() = runTest {
        val result = sut.getDBCount()
        result.test {
            Assert.assertEquals(0, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test insertPokemon should update getDBCount re-actively`() = runTest {
        sut.getDBCount().test {
            Assert.assertEquals(0, awaitItem())

            val newData = listOf(DUMMY_POKEMON_DETAIL.toEntity())
            sut.insertPokemon(newData)

            Assert.assertEquals(1, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test insertPokemon and loadPokemon re-actively`() = runTest {
        val newData = listOf(DUMMY_POKEMON_DETAIL.toEntity())
        sut.insertPokemon(newData)

        val result = sut.loadPokemon()

        result.test {
            Assert.assertEquals(newData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test setLastUpdate and getLastUpdate re-actively`() = runTest {
        val currentTime = 20L
        sut.setLastUpdate(currentTime)

        val result = sut.getLastUpdate()

        result.test {
            Assert.assertEquals(currentTime, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test deletePokemonData re-actively`() = runTest {
        sut.loadPokemon().test {
            Assert.assertEquals(emptyList<PokemonEntity>(), awaitItem())

            val newData = listOf(DUMMY_POKEMON_DETAIL.toEntity())
            sut.insertPokemon(newData)

            Assert.assertEquals(newData, awaitItem())

            sut.deletePokemonData()
            Assert.assertEquals(emptyList<PokemonEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
