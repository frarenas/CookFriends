package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun HomeNavGraph(homeNavController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = homeNavController,
        startDestination = "recipes",
        modifier = androidx.compose.ui.Modifier.padding(paddingValues)
    ) {
        composable("recipes") { RecipesScreen() }
        composable("favorites") { FavoritesScreen() }
        composable("myrecipes") { MyRecipesScreen() }
        //composable("profile") { ProfileScreen(onLogout = { mainNavController.navigate("login") { popUpTo("home") { inclusive = true } } }) }
    }
}

data class NavigationItem(val title: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column {
        Text("Contenido del Perfil")
        Button(onClick = onLogout) {
            Text("Cerrar Sesión")
        }
    }
}
