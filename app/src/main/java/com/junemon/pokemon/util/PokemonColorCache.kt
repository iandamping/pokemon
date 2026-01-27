package com.junemon.pokemon.util

import android.util.LruCache
import androidx.compose.ui.graphics.Color

object PokemonColorCache {
    // Cache capacity: 50 item (adjust your needs)
    private val colorCache = LruCache<String, Color>(100)

    fun get(key: String): Color? = colorCache.get(key)

    fun put(key: String, color: Color) {
        colorCache.put(key, color)
    }
}
