package com.trainingproject.habittrackerapp.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.LogoutUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.GetHabitsUseCase
import com.trainingproject.habittrackerapp.domain.usecase.habits.UpdateHabitUseCase
import com.trainingproject.habittrackerapp.domain.usecase.quotes.GetQuoteUseCase
import com.trainingproject.habittrackerapp.presentation.navigation.Screen
import com.trainingproject.habittrackerapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val motivationQuote: String = "Loading quote...",
    val quoteAuthor: String = "",
    val isQuoteLoading: Boolean = true,
    val event: UiEvent? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _currentUserId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val habits: StateFlow<List<Habit>> = _currentUserId
        .flatMapLatest { userId ->
            if (userId == null)
                flowOf(emptyList())
            else
                getHabitsUseCase(userId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUserId.value = user?.id
                if (user == null) {
                    sendUiEvent(UiEvent.Navigate(Screen.AuthScreen.route))
                }
            }
        }
        loadMotivationQuote()
    }

    fun onHabitClick(habitId: String) {
        viewModelScope.launch {
            savedStateHandle["habitId"] = habitId
            sendUiEvent(UiEvent.Navigate(Screen.HabitDetailScreen.createRoute(habitId)))
        }
    }

    fun onToggleHabitCompletion(habit: Habit) {
        viewModelScope.launch {
            val updatedHabit = if (habit.isCompletedToday()) {
                habit.uncompleteToday()
            } else {
                habit.completeToday()
            }
            updateHabitUseCase(updatedHabit)
        }
    }

    fun onAddHabitClick() {
        viewModelScope.launch {
            sendUiEvent(UiEvent.Navigate(Screen.AddHabitScreen.route))
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch { _currentUserId.emit(null) }
        logoutUseCase()
        sendSnackbar("Logged out successfully.")
    }

    fun loadMotivationQuote() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                motivationQuote = "Loading quote...",
                quoteAuthor = "",
                isQuoteLoading = true
            )

            val result = getQuoteUseCase()

            _uiState.value = _uiState.value.copy(isQuoteLoading = false)

            if (result.isSuccess) {
                val quote = result.getOrNull()
                _uiState.value = _uiState.value.copy(
                    motivationQuote = quote?.quoteText ?: "Failed to load quote.",
                    quoteAuthor = quote?.author ?: ""
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    motivationQuote = "Failed to load quote: ${result.exceptionOrNull()?.localizedMessage}",
                    quoteAuthor = ""
                )
                sendSnackbar("Failed to load motivation quote.")
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