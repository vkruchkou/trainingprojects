package com.trainingproject.habittrackerapp.domain.repository

import com.trainingproject.habittrackerapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    fun isLoggedIn(): Flow<Boolean>
}