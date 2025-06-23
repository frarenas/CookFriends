package com.devapp.cookfriends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devapp.cookfriends.presentation.editrecipe.EditRecipeScreen
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.presentation.login.LoginScreen
import com.devapp.cookfriends.presentation.recoverypassword.RecoveryPasswordScreen
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@Composable
fun AppNavGraph(startDestination: Screen) {
    val mainNavController = rememberNavController()
    NavHost(navController = mainNavController, startDestination = startDestination) {
        composable<Login> {
            LoginScreen(
                navigateToHome = { mainNavController.navigate(Home){ popUpTo(Login) { inclusive = true } } },
                navigateToRecoveryPassword = { mainNavController.navigate(RecoveryPassword) })
        }
        composable<Home> { HomeScreen(
            navigateToNewRecipe = { mainNavController.navigate(EditRecipe()) },
            navigateToLogin = { mainNavController.navigate(Login) { popUpTo(Home) { inclusive = true } } }
        ) }
        //composable<Recipe> { RecipeScreen() }
        composable<RecoveryPassword> { RecoveryPasswordScreen(mainNavController) }
        composable<EditRecipe>(typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)){ backStackEntry ->
            val editRecipe: EditRecipe = backStackEntry.toRoute()
            EditRecipeScreen(recipeId = editRecipe.id, mainNavController = mainNavController)
        }
    }
}
