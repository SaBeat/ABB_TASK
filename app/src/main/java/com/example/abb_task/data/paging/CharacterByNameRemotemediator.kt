package com.example.abb_task.data.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.abb_task.data.db.RickAndMortyDatabase
import com.example.abb_task.data.db.entity.CharacterEntity
import com.example.abb_task.data.db.entity.RemoteKeyEntity
import com.example.abb_task.data.remote.RickAndMortyApi
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class CharacterByNameRemoteMediator(
    private val api: RickAndMortyApi,
    private val db: RickAndMortyDatabase,
    private val characterName: String,
    private val characterGender: String,
    private val characterStatus: String,
    private val characterSpecies: String
) : RemoteMediator<Int, CharacterEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val response = if (characterName.isEmpty())
                api.getCharacters(page = page)
            else {

                api.getCharactersByName(page = page, characterName,characterGender,characterStatus,characterSpecies)
            }

            val isEndOfList =
                response.info.next == null
                        || response.toString().contains("error")
                        || response.results.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.characterDao().clearCharacters()
                    db.remoteKeyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKeyEntity(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeyDao().insertAll(keys)
                db.characterDao().insertCharacters(response.results.map {
                    it.toCharacterEntity()
                })
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeyDao().remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> db.remoteKeyDao().remoteKeysCharacterId(character.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character -> db.remoteKeyDao().remoteKeysCharacterId(character.id) }
    }
}