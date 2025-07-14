package com.devapp.cookfriends.data.remote.service

import com.devapp.cookfriends.data.remote.model.ApiResponse
import com.devapp.cookfriends.data.remote.model.ChangePasswordRequest
import com.devapp.cookfriends.data.remote.model.ChangePasswordResponse
import com.devapp.cookfriends.data.remote.model.CommentModel
import com.devapp.cookfriends.data.remote.model.FavoriteModel
import com.devapp.cookfriends.data.remote.model.LoginResponse
import com.devapp.cookfriends.data.remote.model.RatingModel
import com.devapp.cookfriends.data.remote.model.RecipeModel
import com.devapp.cookfriends.data.remote.model.RecipeTypeModel
import com.devapp.cookfriends.data.remote.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import kotlin.uuid.Uuid

interface CookFriendsService {

    @GET("exec?function=getRecipes")
    suspend fun getRecipes(
        @Query("userid") userId: Uuid?
    ): List<RecipeModel>

    @GET("exec?function=getRecipeTypes")
    suspend fun getRecipeTypes(
    ): List<RecipeTypeModel>

    @GET("exec?function=login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LoginResponse

    @GET("exec?function=getUser")
    suspend fun getUser(
        @Query("username") username: String
    ): retrofit2.Response<UserModel?>

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

    @POST("exec?function=updateUserPassword")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): ChangePasswordResponse

    @POST("exec?function=addComment")
    suspend fun addComment(
        @Body request: CommentModel
    ): ApiResponse

    @POST("exec?function=rateRecipe")
    suspend fun rateRecipe(
        @Body request: RatingModel
    ): ApiResponse
}
