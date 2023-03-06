package com.example.guessinggame

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val _state = MutableStateFlow(GuessingGameState())
    val state = _state.asStateFlow()

    fun updateTextField(userNo: String) {
        _state.update { it.copy(userNumber = userNo) }
    }

    fun resetGame() {
        _state.value = GuessingGameState()
    }

    fun onUserInput(
        userNumber: String,
        context: Context
    ) {
        val userNumberInInt = try {
            userNumber.toInt()
        } catch (e: Exception) {
            0
        }

        if (userNumberInInt !in 1..99) {
            Toast.makeText(
                context,
                "Please enter a number between 0 and 100.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val currentState = _state.value

        val newGuessLeft = currentState.noOfGuessLeft - 1

        val newGameStage = when {
            userNumberInInt == currentState.mysteryNumber -> GameStage.WON
            newGuessLeft == 0 -> GameStage.LOSE
            else -> GameStage.PLAYING
        }

        val newHintDescription = when {
            userNumberInInt > currentState.mysteryNumber -> {
                "Hint\nYou are guessing bigger number than the mystery number."
            }
            userNumberInInt < currentState.mysteryNumber -> {
                "Hint\nYou are guessing smaller number than the mystery number."
            }
            else -> ""
        }

        val newGuessNoList = currentState.guessedNumbersList.plus(userNumberInInt)

        _state.update {
            it.copy(
                userNumber = "",
                noOfGuessLeft = newGuessLeft,
                guessedNumbersList = newGuessNoList,
                hintDescription = newHintDescription,
                gameStage = newGameStage
            )
        }
    }
}