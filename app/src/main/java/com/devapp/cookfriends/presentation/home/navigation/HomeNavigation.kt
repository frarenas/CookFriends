package com.devapp.cookfriends.presentation.home.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devapp.cookfriends.presentation.home.favorites.FavoritesScreen
import com.devapp.cookfriends.presentation.home.myrecipes.MyRecipesScreen
import com.devapp.cookfriends.presentation.home.recipes.RecipesScreen

@Composable
fun HomeNavGraph(homeNavController: NavHostController, paddingValues: PaddingValues) {
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
        //composable("profile") { ProfileScreen(onLogout = { mainNavController.navigate("login") { popUpTo("home") { inclusive = true } } }) }
    }
}

data class NavigationItem(val title: String, val route: HomeSection, val icon: ImageVector)

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column {
        Text("Contenido del Perfil")
        Button(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }
}
