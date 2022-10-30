package com.example.abb_task.domain.use_case

import androidx.paging.PagingData
import com.example.abb_task.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersByName(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(characterName: String,characterGender:String,characterStatus:String,
    characterSpecies:String): Flow<PagingData<com.example.abb_task.domain.model.Character>> =
        characterRepository.getCharactersByName(characterName,characterGender,characterStatus,characterSpecies)
}