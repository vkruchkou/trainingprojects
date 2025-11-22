package com.trainingproject.habittrackerapp.presentation.add_habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.AddHabitUseCase
import com.trainingproject.habittrackerapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddHabitUiState(
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val goalDays: String = "21",
    val isLoading: Boolean = false,
    val event: UiEvent? = null
)

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val addHabitUseCase: AddHabitUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddHabitUiState())
    val uiState: StateFlow<AddHabitUiState> = _uiState

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName)
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.value = _uiState.value.copy(description = newDescription)
    }

    fun onIconUrlChange(newUrl: String) {
        _uiState.value = _uiState.value.copy(iconUrl = newUrl)
    }

    fun onGoalDaysChange(newGoalDays: String) {
        if (newGoalDays.all { it.isDigit() } || newGoalDays.isEmpty()) {
            _uiState.value = _uiState.value.copy(goalDays = newGoalDays)
        }
    }

    fun onSaveHabit() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            sendSnackbar("Habit name cannot be empty.")
            return
        }
        val currentGoalDays = state.goalDays.toIntOrNull() ?: 21

        _uiState.value = state.copy(isLoading = true)

        viewModelScope.launch {
            val userId = getCurrentUserUseCase().first()?.id
            if (userId == null) {
                sendSnackbar("User not logged in. Please re-authenticate.")
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }

            val newHabit = Habit(
                userId = userId,
                name = state.name,
                description = state.description,
                iconUrl = state.iconUrl,
                goalDays = currentGoalDays
            )

            try {
                addHabitUseCase(newHabit)
                sendSnackbar("Habit '${state.name}' created successfully!")
                sendUiEvent(UiEvent.PopBackStack)
            } catch (e: Exception) {
                sendSnackbar("Failed to create habit: ${e.localizedMessage}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onEventConsumed() {
        _uiState.value = _uiState.value.copy(event = null)
    }

    private fun sendUiEvent(event: UiEvent) {
        _uiState.value = _uiState.value.copy(event = event)
    }

    private fun sendSnackbar(message: String) {
        sendUiEvent(UiEvent.ShowSnackbar(message))
    }
}