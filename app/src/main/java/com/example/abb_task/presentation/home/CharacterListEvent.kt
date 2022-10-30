package com.example.abb_task.presentation.home

sealed class CharacterListEvent {
    data class GetAllCharacterByName(val characterName:String,val gender:String,val status:String,val species:String)
        :CharacterListEvent()
}