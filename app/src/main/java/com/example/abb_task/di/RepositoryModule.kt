package com.example.abb_task.di

import com.example.abb_task.data.db.RickAndMortyDatabase
import com.example.abb_task.data.remote.RickAndMortyApi
import com.example.abb_task.data.repository.CharacterRepositoryImpl
import com.example.abb_task.domain.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCharacterRepository(api: RickAndMortyApi,db:RickAndMortyDatabase):CharacterRepository =
        CharacterRepositoryImpl(api,db)
}