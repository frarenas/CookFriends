package com.devapp.cookfriends.presentation.home.navigation

import kotlinx.serialization.Serializable

interface HomeSection

@Serializable
data class Recipes(val isUserLogged: Boolean): HomeSection

@Serializable
data class Favorites(val isUserLogged: Boolean): HomeSection

@Serializable
data class MyRecipes(val isUserLogged: Boolean): HomeSection

@Serializable
object Profile: HomeSection

@Serializable
object RecipeDetail: HomeSection