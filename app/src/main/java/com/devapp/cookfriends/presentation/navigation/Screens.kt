package com.devapp.cookfriends.presentation.navigation

import com.devapp.cookfriends.presentation.home.navigation.HomeSection
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

interface Screen

@Serializable
object Login: Screen

@Serializable
object Home: Screen

@Serializable
object Recipe: Screen

@Serializable
object RecoveryPassword: Screen

@Serializable
data class  EditRecipe (val id: Uuid? = null): Screen

@Serializable
data class IngredientCalculator  (val id: Uuid? = null): Screen

@Serializable
data class RecipeDetail(val id: Uuid): Screen
