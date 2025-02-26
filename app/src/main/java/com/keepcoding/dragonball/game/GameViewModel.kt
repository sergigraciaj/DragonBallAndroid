package com.keepcoding.dragonball.game

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.keepcoding.dragonball.repository.CharactersRepository
import com.keepcoding.dragonball.repository.UserRepository
import com.keepcoding.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    private var token: String? = null

    sealed class State {
        data object Loading: State()
        data class Success(val characters: List<Character>): State()
        data class Error(val message: String): State()
        data class SelectedCharacter(val character: Character): State()
    }

    private val _uiState = MutableStateFlow<State>(State.Loading)
    private val characterRepository = CharactersRepository()
    val userRepository = UserRepository()

    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun damageCharacter(character: Character) {
        character.currentLife -= 10
    }

    fun selectedCharacter(character: Character) {
        character.timesSelected++
        _uiState.value = State.SelectedCharacter(character)
    }

    fun unselectedCharacter() {
        val result = characterRepository.fetchCharacters(userRepository.getToken(),)
        when (result) {
            is CharactersRepository.CharactersResponse.Success -> {
                _uiState.value = State.Success(result.characters)
            }

            is CharactersRepository.CharactersResponse.Error -> {
                _uiState.value = State.Error(result.message)
            }
        }
    }

    fun downloadCharacters(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading
            val result = characterRepository.fetchCharacters(userRepository.getToken(), sharedPreferences)
            when (result) {
                is CharactersRepository.CharactersResponse.Success -> {
                    _uiState.value = State.Success(result.characters)
                }

                is CharactersRepository.CharactersResponse.Error -> {
                    _uiState.value = State.Error(result.message)
                }
            }
        }
    }
}