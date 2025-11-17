package com.trainingproject.habittrackerapp.domain.usecase.auth

import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.register(email, password)
    }
}