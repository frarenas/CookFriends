package com.devapp.cookfriends.data.remote.service

import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import com.devapp.cookfriends.data.remote.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CookFriendsService {

    @GET("exec?function=getRecipes")
    suspend fun getRecipes(
    ): List<RecipeModel>

    @GET("exec?function=getRecipeTypes")
    suspend fun getRecipeTypes(
    ): List<RecipeTypeModel>

    @GET("exec?function=getUser")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): UserModel?
}
