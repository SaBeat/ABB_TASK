package com.example.abb_task.presentation.home

import androidx.paging.PagingData

sealed class CharacterListState{
    data class Success(val charactersPaged: PagingData<com.example.abb_task.domain.model.Character>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
    object Loading : CharacterListState()
}