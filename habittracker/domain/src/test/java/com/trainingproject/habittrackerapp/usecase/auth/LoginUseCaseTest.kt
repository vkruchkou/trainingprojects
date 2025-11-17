package com.trainingproject.habittrackerapp.usecase.auth

import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import com.trainingproject.habittrackerapp.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var mockAuthRepository: AuthRepository


    @Before
    fun setUp() {
        mockAuthRepository = mock()
        loginUseCase = LoginUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke returns success when repository succeeds login`() = runTest {
        whenever(mockAuthRepository.login("", "")).thenReturn(Result.success(Unit))

        Assert.assertTrue(loginUseCase("", "").isSuccess)
    }

    @Test
    fun `invoke returns failure when repository fails login`() = runTest {
        whenever(mockAuthRepository.login("", "")).thenReturn(Result.failure(RuntimeException()))

        Assert.assertTrue(loginUseCase("", "").isFailure)
    }
}