package com.devapp.cookfriends.data.service

import com.devapp.cookfriends.data.model.RecipeModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CookFriendsService {

    @GET("exec?function=getAllRecipes")
    suspend fun getRecipes(
        @Query("param_name") param: String = "value"
    ): List<RecipeModel>
}
