package com.junemon.pokemon.core.di

import android.content.Context
import androidx.room.Room
import com.junemon.pokemon.core.data.dataSource.local.PokemonDao
import com.junemon.pokemon.core.data.dataSource.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): PokemonDatabase {
        return Room
            .databaseBuilder(context, PokemonDatabase::class.java, "pokemon.db")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun providePokemonDao(db: PokemonDatabase): PokemonDao {
        return db.pokemonDao()
    }
}
