package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val service: CookFriendsService
) {

    suspend fun getRecipes(): List<RecipeModel> {

        return service.getRecipes()
    }
}
