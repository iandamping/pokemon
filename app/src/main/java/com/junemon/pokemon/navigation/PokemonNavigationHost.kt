package com.junemon.pokemon.navigation

import androidx.compose.animation.SharedTransitionLayout
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
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.HomeScreen,
            modifier = modifier
        ) {
            composable<NavigationScreen.HomeScreen> {
                HomeRoute(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onMoveToDetailScreen = { pokemonId ->
                        navController.navigate(NavigationScreen.DetailScreen(pokemonId = pokemonId))
                    }
                )
            }

            composable<NavigationScreen.DetailScreen> {
                DetailRoute(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                )
            }
        }
    }
}
