package com.trainingproject.habittrackerapp.domain.usecase.auth

import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase@Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        return authRepository.logout()
    }
}