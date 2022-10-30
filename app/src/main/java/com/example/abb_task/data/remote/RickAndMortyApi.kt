package com.example.abb_task.data.remote

import com.example.abb_task.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page")
        page: Int
    ): CharacterResponseDto

    @GET("character/")
    suspend fun getCharactersByName(
        @Query("page")
        page: Int,
        @Query("name") characterName: String,
        @Query("gender") characterGender: String,
        @Query("status") characterStatus: String,
        @Query("species") characterSpecies: String,
    ): CharacterResponseDto

//    @GET("character/")
//    suspend fun getCharactersBySpecies(
//        @Query("page")
//        page: Int,
//        @Query("species")
//        characterName: String
//    ): CharacterResponseDto
//
//    @GET("character/")
//    suspend fun getCharactersByGender(
//        @Query("page")
//        page: Int,
//        @Query("gender")
//        characterName: String
//    ): CharacterResponseDto
//
//    @GET("character/")
//    suspend fun getCharactersByStatus(
//        @Query("page")
//        page: Int,
//        @Query("status")
//        characterName: String
//    ): CharacterResponseDto


    companion object {
        const val BASE_URL_API = "https://rickandmortyapi.com/api/"
    }
}