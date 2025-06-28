package com.devapp.cookfriends.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.devapp.cookfriends.presentation.home.favorites.FavoritesScreen
import com.devapp.cookfriends.presentation.home.myrecipes.MyRecipesScreen
import com.devapp.cookfriends.presentation.home.recipes.RecipesScreen
import com.devapp.cookfriends.presentation.navigation.RecipeDetail
import com.devapp.cookfriends.presentation.navigation.UuidNavType
import com.devapp.cookfriends.presentation.profile.ProfileScreen
import com.devapp.cookfriends.presentation.recipeDetail.RecipeDetailScreen
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@Composable
fun HomeNavGraph(
    homeNavController: NavHostController,
    navigateToLogin: () -> Unit,
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
            RecipesScreen(
                isUserLogged = isUserLogged,
                mainNavController = homeNavController
            )
        }
        composable<Favorites> {
            FavoritesScreen(isUserLogged = isUserLogged)
        }
        composable<MyRecipes> {
            MyRecipesScreen(isUserLogged = isUserLogged)
        }
        composable<Profile> {
            ProfileScreen(
                snackbarHostState = snackbarHostState,
            ){ navigateToLogin () }
        }

        composable<RecipeDetail>(typeMap = mapOf(typeOf<Uuid>() to UuidNavType)) {
            val args = it.toRoute<RecipeDetail>()
            RecipeDetailScreen(
                recipeId = args.id,
                navigateBack = { homeNavController.popBackStack() }
            )
        }
    }
}
