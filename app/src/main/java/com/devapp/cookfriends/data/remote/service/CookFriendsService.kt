package com.devapp.cookfriends.data.remote.service

import com.devapp.cookfriends.data.remote.model.ApiResponse
import com.devapp.cookfriends.data.remote.model.FavoriteModel
import com.devapp.cookfriends.data.remote.model.LoginResponse
import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CookFriendsService {

    @GET("exec?function=getRecipes")
    suspend fun getRecipes(
    ): List<RecipeModel>

    @GET("exec?function=getRecipeTypes")
    suspend fun getRecipeTypes(
    ): List<RecipeTypeModel>

    @GET("exec?function=login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LoginResponse

    @POST("exec?function=addFavorite")
    suspend fun addFavorite(
        @Body favorite: FavoriteModel
    ): ApiResponse

    @POST("exec?function=removeFavorite")
    suspend fun removeFavorite(
        @Body favorite: FavoriteModel
    ): ApiResponse

    @POST("exec?function=upsertRecipe")
    suspend fun upsertRecipe(
        @Body recipe: RecipeModel
    ): ApiResponse
}
