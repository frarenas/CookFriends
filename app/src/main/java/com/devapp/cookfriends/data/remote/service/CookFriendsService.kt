package com.devapp.cookfriends.data.remote.service

import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CookFriendsService {

    @GET("exec?function=getRecipes")
    suspend fun getRecipes(
        @Query("param_name") param: String = "value"
    ): List<RecipeModel>

    @GET("exec?function=getRecipeTypes")
    suspend fun getRecipeTypes(
    ): List<RecipeTypeModel>
}
