package com.example.abb_task.di

import com.example.abb_task.domain.repository.CharacterRepository
import com.example.abb_task.domain.use_case.GetCharactersByName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetCharactersByNameUseCase(characterRepository: CharacterRepository):GetCharactersByName =
        GetCharactersByName(characterRepository)
}