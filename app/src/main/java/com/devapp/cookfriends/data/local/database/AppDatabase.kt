package com.devapp.cookfriends.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devapp.cookfriends.data.local.dao.RecipeDao
import com.devapp.cookfriends.data.local.dao.RecipeTypeDao
import com.devapp.cookfriends.data.local.entity.CommentEntity
import com.devapp.cookfriends.data.local.entity.FavoriteEntity
import com.devapp.cookfriends.data.local.entity.IngredientEntity
import com.devapp.cookfriends.data.local.entity.RecipePhotoEntity
import com.devapp.cookfriends.data.local.entity.RatingEntity
import com.devapp.cookfriends.data.local.entity.RecipeEntity
import com.devapp.cookfriends.data.local.entity.RecipeTypeEntity
import com.devapp.cookfriends.data.local.entity.StepEntity
import com.devapp.cookfriends.data.local.entity.StepPhotoEntity
import com.devapp.cookfriends.data.local.entity.UserEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        StepEntity::class,
        RecipePhotoEntity::class,
        RatingEntity::class,
        CommentEntity::class,
        FavoriteEntity::class,
        RecipeTypeEntity::class,
        UserEntity::class,
        StepPhotoEntity::class
    ],
    version = 18,
    exportSchema = false)
@TypeConverters(UuidConverter::class, InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun recipeTypeDao(): RecipeTypeDao
}
