package com.trainingproject.habittrackerapp.domain.usecase.auth

import com.trainingproject.habittrackerapp.domain.models.User
import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }
}