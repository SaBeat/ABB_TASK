package com.example.abb_task.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abb_task.data.db.entity.CharacterEntity

@Dao
interface CharacterDao {
    @Query("Select * from tb_character where name LIKE '%' || :characterName || '%'")
    fun getCharactersByName(characterName: String): PagingSource<Int, CharacterEntity>

//    @Query("Select * from tb_character where name LIKE '%' || :gender || '%'")
//    fun getCharactersByGender(gender: String): PagingSource<Int, CharacterEntity>
//
//    @Query("Select * from tb_character where name LIKE '%' || :species || '%'")
//    fun getCharactersBySpecies(species: String): PagingSource<Int, CharacterEntity>
//
//    @Query("Select * from tb_character where name LIKE '%' || :status || '%'")
//    fun getCharactersByStatus(status: String): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM tb_character")
    suspend fun clearCharacters()
}