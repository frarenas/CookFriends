package com.devapp.cookfriends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cook_friends.presentation.screens.LoginScreen
import com.devapp.cookfriends.presentation.editrecipe.EditRecipeScreen
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.presentation.recoverypassword.RecoveryPasswordScreen
import kotlin.reflect.typeOf
import kotlin.uuid.Uuid

@Composable
fun AppNavGraph() {
    val mainNavController = rememberNavController()
    NavHost(navController = mainNavController, startDestination = EditRecipe(id = Uuid.parse("34c0730c-8373-4789-9506-e4b2ba33124b"))) {
        composable<Login> { LoginScreen(Modifier, mainNavController) }
        composable<Home> { HomeScreen(mainNavController = mainNavController) }
        //composable<Recipe> { RecipeScreen() }
        composable<RecoveryPassword> { RecoveryPasswordScreen(mainNavController) }
        /*composable<EditRecipe> { backStackEntry ->
            val editRecipe: EditRecipe = backStackEntry.toRoute()
            EditRecipeScreen(recipeId = editRecipe.id, mainNavController = mainNavController)
        }*/

        composable<EditRecipe>(typeMap = mapOf(typeOf<Uuid?>() to UuidNavType)){ backStackEntry ->
            val editRecipe: EditRecipe = backStackEntry.toRoute()
            EditRecipeScreen(recipeId = editRecipe.id, mainNavController = mainNavController)
        }
    }
}
