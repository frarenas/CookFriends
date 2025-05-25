package com.devapp.cookfriends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.presentation.login.LoginScreen
import com.devapp.cookfriends.presentation.recoverypassword.RecoveryPasswordScreen

@Composable
fun AppNavGraph() {
    val mainNavController = rememberNavController()
    NavHost(navController = mainNavController, startDestination = Home) {
        composable<Login> { LoginScreen(Modifier) }
        composable<Home> { HomeScreen(mainNavController = mainNavController) }
        //composable<Recipe> { RecipeScreen() }
        composable<RecoveryPassword> { RecoveryPasswordScreen() }
    }
}
