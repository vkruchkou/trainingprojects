package com.trainingproject.habittrackerapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.LoginUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.RegisterUseCase
import com.trainingproject.habittrackerapp.presentation.navigation.Screen
import com.trainingproject.habittrackerapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isRegistering: Boolean = false,
    val event: UiEvent? = null
)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase().first()
            if (currentUser != null) {
                sendUiEvent (UiEvent.Navigate(Screen.HomeScreen.route))
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            sendSnackbar("Email and password cannot be empty.")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = loginUseCase(currentState.email, currentState.password)

            _uiState.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                sendSnackbar("Login successful!")
                sendUiEvent (UiEvent.Navigate(Screen.HomeScreen.route))
            } else {
                sendSnackbar("Login failed: ${result.exceptionOrNull()?.localizedMessage}")
            }
        }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value

        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            sendSnackbar("Email and password cannot be empty.")
            return
        }

        _uiState.update { it.copy(isLoading = true, isRegistering = true) }

        viewModelScope.launch {
            val result = registerUseCase(currentState.email, currentState.password)

            _uiState.update { it.copy(isLoading = false, isRegistering = false) }

            if (result.isSuccess) {
                sendSnackbar("Registration successful! You can now log in.")
                sendUiEvent (UiEvent.Navigate(Screen.HomeScreen.route))
            } else {
                sendSnackbar("Registration failed: ${result.exceptionOrNull()?.localizedMessage}")
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        _uiState.update {
            it.copy(
                event = event
            )
        }
    }
    private fun sendSnackbar(message: String) {
        sendUiEvent(UiEvent.ShowSnackbar(message))
    }

    fun onEventConsumed() {
        _uiState.value = _uiState.value.copy(event = null)
    }
}