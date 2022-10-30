package com.example.abb_task.di

import android.app.Application
import androidx.room.Room
import com.example.abb_task.data.db.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRickAndMortyDatabase(
        app: Application
    ): RickAndMortyDatabase =
        Room.databaseBuilder(app, RickAndMortyDatabase::class.java, "db_rick_morty").build()
}