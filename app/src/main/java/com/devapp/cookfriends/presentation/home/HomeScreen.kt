package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devapp.cookfriends.R
import com.devapp.cookfriends.presentation.common.MessageScreen
import com.devapp.cookfriends.presentation.home.navigation.Favorites
import com.devapp.cookfriends.presentation.home.navigation.HomeNavGraph
import com.devapp.cookfriends.presentation.home.navigation.MyRecipes
import com.devapp.cookfriends.presentation.home.navigation.NavigationItem
import com.devapp.cookfriends.presentation.home.navigation.Profile
import com.devapp.cookfriends.presentation.home.navigation.Recipes

@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val recipesState by viewModel.recipesState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val homeNavController = rememberNavController()
    val items = listOf(
        NavigationItem(stringResource(R.string.recipes), Recipes, Icons.Outlined.Kitchen),
        NavigationItem(stringResource(R.string.favorites), Favorites, Icons.Sharp.Favorite),
        NavigationItem(stringResource(R.string.my_recipes), MyRecipes, Icons.Outlined.Book),
        NavigationItem(stringResource(R.string.profile), Profile, Icons.Outlined.Person)
    )
    var selectedItem by remember { mutableStateOf(items[0]) }
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Box(modifier = Modifier.fillMaxSize()) {
        if (recipesState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (recipesState.error != null) {
            MessageScreen(
                message = recipesState.error!!,
                imageVector = Icons.Default.Error
            )
        } else {
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
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                HomeNavGraph(
                    mainNavController = mainNavController,
                    homeNavController = homeNavController,
                    paddingValues = innerPadding,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
