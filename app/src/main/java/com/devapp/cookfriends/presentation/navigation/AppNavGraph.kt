package com.devapp.cookfriends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devapp.cookfriends.presentation.editrecipe.EditRecipeScreen
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.presentation.ingredientcalculator.IngredientCalculatorScreen
import com.devapp.cookfriends.presentation.login.LoginScreen
import com.devapp.cookfriends.presentation.recoverypassword.RecoveryPasswordScreen
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@Composable
fun AppNavGraph(startDestination: Screen) {
    val mainNavController = rememberNavController()
    NavHost(navController = mainNavController, startDestination = startDestination) {
        //NavHost(navController = mainNavController, startDestination = IngredientCalculator(id = Uuid.parse("a5e1edb0-4028-4668-a392-e32727dc1424"))) {
        composable<Login> {
            LoginScreen(
                navigateToHome = { mainNavController.navigate(Home){ popUpTo(Login) { inclusive = true } } },
                navigateToRecoveryPassword = { mainNavController.navigate(RecoveryPassword) })
        }
        composable<Home> { HomeScreen(mainNavController = mainNavController) }
        //composable<Recipe> { RecipeScreen() }
        composable<RecoveryPassword> { RecoveryPasswordScreen(mainNavController) }
        composable<EditRecipe>(typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)){ backStackEntry ->
            val editRecipe: EditRecipe = backStackEntry.toRoute()
            EditRecipeScreen(recipeId = editRecipe.id, mainNavController = mainNavController)
        }
        composable<IngredientCalculator>(typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)) {
                backStackEntry ->
            val ingredientCalculator: EditRecipe = backStackEntry.toRoute()
            IngredientCalculatorScreen(recipeId = ingredientCalculator.id, mainNavController = mainNavController)
        }
    }
}
