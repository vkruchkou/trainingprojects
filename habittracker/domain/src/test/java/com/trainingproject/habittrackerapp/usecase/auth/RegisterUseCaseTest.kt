package com.trainingproject.habittrackerapp.usecase.auth

import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import com.trainingproject.habittrackerapp.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class RegisterUseCaseTest {
    private lateinit var mockAuthRepository: AuthRepository
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setUp() {
        mockAuthRepository = mock()
        registerUseCase = RegisterUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke returns success when repository succeeds register`() = runTest {
        whenever(mockAuthRepository.register("", "")).thenReturn(Result.success(Unit))

        Assert.assertTrue(registerUseCase("", "").isSuccess)
    }

    @Test
    fun `invoke returns failure when repository fails register`() = runTest {
        whenever(mockAuthRepository.register("", "")).thenReturn(Result.failure(RuntimeException()))

        Assert.assertTrue(registerUseCase("", "").isFailure)
    }
}