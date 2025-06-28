package com.devapp.cookfriends.presentation.home.navigation

import com.devapp.cookfriends.presentation.navigation.Screen
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

interface HomeSection

@Serializable
data class Recipes(val isUserLogged: Boolean): HomeSection

@Serializable
data class Favorites(val isUserLogged: Boolean): HomeSection

@Serializable
data class MyRecipes(val isUserLogged: Boolean): HomeSection

@Serializable
object Profile: HomeSection