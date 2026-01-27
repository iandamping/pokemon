package com.junemon.pokemon.util.pokemonColorExtractor

import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.Image
import coil3.toBitmap
import com.junemon.pokemon.core.di.IoDispatcher
import com.junemon.pokemon.util.PokemonColorCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonColorExtractorImpl @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : PokemonColorExtractor {

    override suspend fun extractColorFromImage(pokemonId: String, image: Image): Color =
        withContext(ioDispatcher) {
            // 1. Used cache color first from LruCache if exist
            PokemonColorCache.get(pokemonId)
                ?.let { cachedColor -> return@withContext cachedColor }
            // 2. if not exist cache, do heavy bitmap computation here
            val bitmap = image.toBitmap()
            val palette = Palette.from(bitmap).generate()

            val swatch =
                palette.vibrantSwatch ?: palette.lightVibrantSwatch ?: palette.mutedSwatch

            val extractedColor = swatch?.let { Color(it.rgb) } ?: Color.LightGray
            // 3. Save palette colors into cache for next usage
            PokemonColorCache.put(pokemonId, extractedColor)
            extractedColor
        }
}
