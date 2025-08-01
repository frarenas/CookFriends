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
import com.devapp.cookfriends.presentation.profile.ProfileScreen
import kotlin.uuid.Uuid

@Composable
fun HomeNavGraph(
    homeNavController: NavHostController,
    navigateToLogin: () -> Unit,
    navigateToDetail: (recipeId: Uuid) -> Unit,
    isUserLogged: Boolean = false,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = homeNavController,
        startDestination = Recipes(isUserLogged = isUserLogged),
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<Recipes> {
            RecipesScreen(isUserLogged = isUserLogged, navigateToDetail = navigateToDetail)
        }
        composable<Favorites> {
            FavoritesScreen(isUserLogged = isUserLogged, navigateToDetail = navigateToDetail)
        }
        composable<MyRecipes> {
            MyRecipesScreen(isUserLogged = isUserLogged, snackbarHostState = snackbarHostState, navigateToDetail = navigateToDetail)
        }
        composable<Profile> {
            ProfileScreen(
                snackbarHostState = snackbarHostState,
            ) { navigateToLogin() }
        }
    }
}
