package com.junemon.pokemon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.junemon.pokemon.feature.detail.DetailRoute
import com.junemon.pokemon.feature.home.HomeRoute

@Composable
fun PokemonNavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen,
        modifier = modifier
    ) {
        composable<HomeScreen> {
            HomeRoute(onMoveToDetailScreen = { pokemonId ->
                navController.navigate(DetailScreen(pokemonId = pokemonId))
            })
        }

        composable<DetailScreen> {
            DetailRoute()
        }
    }
}
