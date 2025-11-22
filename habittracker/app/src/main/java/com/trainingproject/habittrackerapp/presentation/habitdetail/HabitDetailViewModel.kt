package com.trainingproject.habittrackerapp.presentation.habitdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.usecase.habits.DeleteHabitUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.GetHabitByIdUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.UpdateHabitUseCase
import com.trainingproject.habittrackerapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitDetailUiState(
    val habit: Habit? = null,
    val isLoading: Boolean = false,
    val event: UiEvent? = null
)
@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitDetailUiState())
    val uiState: StateFlow<HabitDetailUiState> = _uiState.asStateFlow()

    init {
        val habitId = savedStateHandle.get<String>("habitId")
        if (habitId != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val habitResult = getHabitByIdUseCase(habitId)
                    _uiState.update { it.copy(habit = habitResult) }
                } catch (e: Exception) {
                    sendSnackbar("Habit not found. ${e.message}")
                    sendUiEvent(UiEvent.PopBackStack)
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            sendSnackbar("Habit ID not provided.")
            sendUiEvent(UiEvent.PopBackStack)
        }
    }

    fun onToggleHabitCompletion(habitToUpdate: Habit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val updatedHabit = if (habitToUpdate.isCompletedToday()) {
                habitToUpdate.uncompleteToday()
            } else {
                habitToUpdate.completeToday()
            }
            try {
                updateHabitUseCase(updatedHabit)
                _uiState.update { it.copy(habit = updatedHabit) }
                sendSnackbar("Habit updated.")
            } catch (e: Exception) {
                sendSnackbar("Failed to update habit: ${e.localizedMessage}")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDeleteHabit() {
        val currentHabit = _uiState.value.habit ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deleteHabitUseCase(currentHabit.id)
                sendSnackbar("Habit '${currentHabit.name}' deleted.")
                sendUiEvent(UiEvent.PopBackStack)
            } catch (e: Exception) {
                sendSnackbar("Failed to delete habit: ${e.localizedMessage}")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        _uiState.value = _uiState.value.copy(event = event)
    }
    private fun sendSnackbar(message: String) {
        sendUiEvent(UiEvent.ShowSnackbar(message))
    }

    fun onEventConsumed() {
        _uiState.value = _uiState.value.copy(event = null)
    }
}
