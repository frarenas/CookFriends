package com.devapp.cookfriends.presentation.home.navigation

import kotlinx.serialization.Serializable

interface HomeSection

@Serializable
object Recipes: HomeSection

@Serializable
object Favorites: HomeSection

@Serializable
object MyRecipes: HomeSection

@Serializable
object Profile: HomeSection

@Serializable
object RecipeDetail: HomeSection