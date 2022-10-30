package com.example.abb_task.domain.repository

import androidx.paging.PagingData
import com.example.abb_task.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharactersByName(characterName: String,characterGender: String,
       characterStatus: String,characterSpecies: String): Flow<PagingData<com.example.abb_task.domain.model.Character>>
}