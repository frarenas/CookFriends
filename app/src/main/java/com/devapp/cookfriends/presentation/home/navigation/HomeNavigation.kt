package com.devapp.cookfriends.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devapp.cookfriends.presentation.home.favorites.FavoritesScreen
import com.devapp.cookfriends.presentation.home.myrecipes.MyRecipesScreen
import com.devapp.cookfriends.presentation.home.recipes.RecipesScreen
import com.devapp.cookfriends.presentation.navigation.Home
import com.devapp.cookfriends.presentation.navigation.Login
import com.devapp.cookfriends.presentation.profile.ProfileScreen

@Composable
fun HomeNavGraph(
    mainNavController: NavHostController,
    homeNavController: NavHostController,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = homeNavController,
        startDestination = Recipes,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<Recipes> {
            RecipesScreen()
        }
        composable<Favorites> {
            FavoritesScreen()
        }
        composable<MyRecipes> {
            MyRecipesScreen()
        }
        composable<Profile> {
            ProfileScreen(
                snackbarHostState = snackbarHostState,
                navController = mainNavController)
            { mainNavController.navigate(Login) { popUpTo(Home) { inclusive = true } } }
        }
    }
}
