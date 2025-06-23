package com.devapp.cookfriends.presentation.home.navigation

import kotlinx.serialization.Serializable

interface HomeSection

@Serializable
data class  Recipes(val isUserLogged: Boolean): HomeSection

@Serializable
object Favorites: HomeSection

@Serializable
object MyRecipes: HomeSection

@Serializable
object Profile: HomeSection