package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.devapp.cookfriends.ui.theme.LightBlue50
import kotlin.uuid.Uuid

@Composable
fun HomeScreen(
    navigateToNewRecipe: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToDetail: (recipeId: Uuid) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recipesState by viewModel.recipesState.collectAsState()
    val isUserLogged by viewModel.isUserLogged.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val homeNavController = rememberNavController()
    val items = listOf(
        NavigationItem(
            stringResource(R.string.recipes),
            Recipes(isUserLogged = isUserLogged),
            Icons.Outlined.Kitchen
        ),
        NavigationItem(
            stringResource(R.string.favorites),
            Favorites(isUserLogged = isUserLogged),
            Icons.Outlined.FavoriteBorder
        ),
        NavigationItem(
            stringResource(R.string.my_recipes),
            MyRecipes(isUserLogged = isUserLogged),
            Icons.Outlined.Book
        ),
        NavigationItem(stringResource(R.string.profile), Profile, Icons.Outlined.Person)
    )
    var selectedItemPosition by rememberSaveable { mutableIntStateOf(0) }
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        if (recipesState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (recipesState.message?.blocking == true) {
            MessageScreen(
                message = recipesState.message!!.uiText.asString(context),
                imageVector = Icons.Default.Error
            )
        } else {
            LaunchedEffect(key1 = recipesState.message) {
                recipesState.message?.let {
                    snackbarHostState.showSnackbar(
                        message = it.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (isUserLogged) {
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets,
                            containerColor = LightBlue50
                        ) {
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title) },
                                    selected = currentRoute?.startsWith(item.route.javaClass.name) == true,
                                    onClick = {
                                        selectedItemPosition = items.indexOf(item)
                                        homeNavController.navigate(item.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(homeNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                floatingActionButton = {
                    if (isUserLogged && selectedItemPosition == 2) {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = { navigateToNewRecipe() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.new_recipe)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                HomeNavGraph(
                    homeNavController = homeNavController,
                    navigateToLogin = navigateToLogin,
                    navigateToDetail = navigateToDetail,
                    isUserLogged = isUserLogged,
                    paddingValues = innerPadding,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
