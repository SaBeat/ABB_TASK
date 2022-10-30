package com.example.abb_task.data.repository

import androidx.paging.*
import com.example.abb_task.data.db.RickAndMortyDatabase
import com.example.abb_task.data.paging.CharacterByNameRemoteMediator
import com.example.abb_task.data.remote.RickAndMortyApi
import com.example.abb_task.domain.model.Character
import com.example.abb_task.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
   private val api:RickAndMortyApi,
   private val db:RickAndMortyDatabase
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersByName(characterName: String,characterGender: String,characterStatus: String,
    characterSpecies: String): Flow<PagingData<com.example.abb_task.domain.model.Character>> {
        val pagingSourceFactory = { db.characterDao().getCharactersByName(characterName) }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                jumpThreshold = Int.MIN_VALUE,
                enablePlaceholders = true,
            ),
            remoteMediator = CharacterByNameRemoteMediator(
                api,
                db,
                characterName,
                characterGender,
                characterStatus,characterSpecies
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { CharacterEntityPagingData ->
            CharacterEntityPagingData.map { characterEntity -> characterEntity.toCharacter() }
        }
    }

}