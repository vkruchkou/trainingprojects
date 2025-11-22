package com.trainingproject.data.repository

import com.trainingproject.data.local.FirebaseAuthSource
import com.trainingproject.habittrackerapp.domain.models.User
import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return firebaseAuthSource.loginWithEmailAndPassword(email, password)
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        return firebaseAuthSource.registerWithEmailAndPassword(email, password)
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        // TODO: Implement Google Sign-in logic using FirebaseAuthSource.
        return Result.failure(UnsupportedOperationException("Google Sign-in not yet implemented"))
    }

    override fun logout() {
        firebaseAuthSource.logout()
    }

    override fun getCurrentUser(): Flow<User?> {
        return firebaseAuthSource.getCurrentUserFlow()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return firebaseAuthSource.isLoggedInFlow()
    }
}
