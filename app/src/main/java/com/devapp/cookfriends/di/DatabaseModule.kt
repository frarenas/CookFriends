package com.devapp.cookfriends.di

import android.content.Context
import androidx.room.Room
import com.devapp.cookfriends.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    const val DB_NAME = "cook_friends_db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideSomeDao(appDatabase: AppDatabase) = appDatabase.recipeDao()
}
