package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.home.navigation.Favorites
import com.devapp.cookfriends.presentation.home.navigation.HomeNavGraph
import com.devapp.cookfriends.presentation.home.navigation.MyRecipes
import com.devapp.cookfriends.presentation.home.navigation.NavigationItem
import com.devapp.cookfriends.presentation.home.navigation.Profile
import com.devapp.cookfriends.presentation.home.navigation.Recipes

@Composable
fun HomeScreen(mainNavController: NavHostController) {

    val homeNavController = rememberNavController()
    val items = listOf(
        NavigationItem(stringResource(R.string.recipes), Recipes, Icons.Outlined.Home),
        NavigationItem(stringResource(R.string.favorites), Favorites, Icons.Outlined.Favorite),
        NavigationItem(stringResource(R.string.my_recipes), MyRecipes, Icons.Outlined.Favorite),
        NavigationItem(stringResource(R.string.profile), Profile, Icons.Outlined.Person)
    )
    var selectedItem by remember { mutableStateOf(items[0]) }
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route.javaClass.name,
                        onClick = {
                            selectedItem = item
                            homeNavController.navigate(item.route) {
                                // Evitar que se creen muchas instancias del mismo destino al re-seleccionar
                                launchSingleTop = true
                                // Restaurar el estado al re-seleccionar un item previamente seleccionado
                                restoreState = true
                                // Limpiar la pila de navegación al seleccionar un nuevo item raíz
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeNavGraph(
            mainNavController = mainNavController,
            homeNavController = homeNavController,
            paddingValues = innerPadding
        )
    }
}
