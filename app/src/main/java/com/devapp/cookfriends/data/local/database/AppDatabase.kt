package com.devapp.cookfriends.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devapp.cookfriends.data.local.dao.RecipeDao
import com.devapp.cookfriends.data.local.entity.IngredientEntity
import com.devapp.cookfriends.data.local.entity.PhotoEntity
import com.devapp.cookfriends.data.local.entity.RecipeEntity
import com.devapp.cookfriends.data.local.entity.StepEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        StepEntity::class,
        PhotoEntity::class
    ],
    version = 4,
    exportSchema = false)
@TypeConverters(UuidConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}
