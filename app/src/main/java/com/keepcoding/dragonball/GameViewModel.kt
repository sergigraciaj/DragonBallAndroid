package com.keepcoding.dragonball

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.keepcoding.model.Character
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    sealed class State {
        data object Loading: State()
        data class Success(val characters: List<Character>): State()
        data class Error(val message: String): State()

    }


    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _uiState.asStateFlow()


    fun downloadCharacters() {
        viewModelScope.launch {
            _uiState.value = State.Loading
            delay(2000L)
            _uiState.value = State.Success(fetchCharacters())
        }

    }

    private fun fetchCharacters()= listOf(
        Character("1", "Goku", "https://cdn.alfabetajuega.com/alfabetajuega/2020/12/goku1.jpg?width=300", 100,100),
        Character("2", "Vegeta", " https://cdn.alfabetajuega.com/alfabetajuega/2020/12/vegetita.jpg?width=300", 100,100),
        Character("3", "C-17", "https://www.google.com", 100,100),
        Character("4", "C-18", "https://www.google.com", 100,100),
    )


}