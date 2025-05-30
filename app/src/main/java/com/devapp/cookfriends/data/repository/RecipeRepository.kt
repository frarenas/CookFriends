package com.devapp.cookfriends.data.repository

import com.devapp.cookfriends.data.model.RecipeModel
import com.devapp.cookfriends.data.service.CookFriendsService
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val service: CookFriendsService
) {

    suspend fun getRecipes(): List<RecipeModel> {

        return service.getRecipes()
    }
}
