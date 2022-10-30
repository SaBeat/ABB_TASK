package com.example.abb_task.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.abb_task.domain.use_case.GetCharactersByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    val getCharactersByNameUseCase: GetCharactersByName
) : ViewModel() {
    private val _charactersFlow =
        MutableSharedFlow<PagingData<com.example.abb_task.domain.model.Character>>()
    val charactersFlow = _charactersFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _genderQuery: MutableStateFlow<String> = MutableStateFlow("")
    val genderQuery = _genderQuery.asStateFlow()

    private val _statusQuery: MutableStateFlow<String> = MutableStateFlow("")
    val statusQuery = _statusQuery.asStateFlow()

    private val _speciesQuery: MutableStateFlow<String> = MutableStateFlow("")
    val speciesQuery = _speciesQuery.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.GetAllCharacterByName -> onSearch(event.characterName,event.gender,event.status,event.species)
        }
    }

    init {
        getCharactersByName("","","","")
    }

    private fun getCharactersByName(
        characterName: String, characterGender: String,
        characterStatus: String, characterSpecies: String
    ) {
        getCharactersByNameUseCase(
            characterName,
            characterGender,
            characterStatus,
            characterSpecies
        ).onEach {
            _charactersFlow.emit(it)
        }.launchIn(viewModelScope)
    }


    private fun onSearch(query: String, gender: String, status: String, species: String) {
        _searchQuery.value = query
        _genderQuery.value = gender
        _statusQuery.value = status
        _speciesQuery.value = species
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getCharactersByName(query, gender, status, species)
        }
    }
}