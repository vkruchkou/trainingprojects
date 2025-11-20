package com.trainingproject.habittrackerapp.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.LoginUseCase
import com.trainingproject.habittrackerapp.domain.usecase.auth.RegisterUseCase
import com.trainingproject.habittrackerapp.presentation.navigation.Screen
import com.trainingproject.habittrackerapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isRegistering by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase().first()
            if (currentUser != null) {
                _uiEvent.send(UiEvent.Navigate(Screen.HomeScreen.route))
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            sendSnackbar("Email and password cannot be empty.")
            return
        }
        isLoading = true
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            isLoading = false
            if (result.isSuccess) {
                sendSnackbar("Login successful!")
                _uiEvent.send(UiEvent.Navigate(Screen.HomeScreen.route))
            } else {
                sendSnackbar("Login failed: ${result.exceptionOrNull()?.localizedMessage}")
            }
        }
    }

    fun onRegisterClick() {
        if (email.isBlank() || password.isBlank()) {
            sendSnackbar("Email and password cannot be empty.")
            return
        }
        isLoading = true
        isRegistering = true
        viewModelScope.launch {
            val result = registerUseCase(email, password)
            isLoading = false
            isRegistering = false
            if (result.isSuccess) {
                sendSnackbar("Registration successful! You can now log in.")
                _uiEvent.send(UiEvent.Navigate(Screen.HomeScreen.route))
            } else {
                sendSnackbar("Registration failed: ${result.exceptionOrNull()?.localizedMessage}")
            }
        }
    }

    private fun sendSnackbar(message: String) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ShowSnackbar(message))
        }
    }
}