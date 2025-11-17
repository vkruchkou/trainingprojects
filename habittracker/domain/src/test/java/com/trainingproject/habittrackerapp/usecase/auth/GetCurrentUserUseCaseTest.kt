package com.trainingproject.habittrackerapp.usecase.auth

import com.trainingproject.habittrackerapp.domain.models.User
import com.trainingproject.habittrackerapp.domain.repository.AuthRepository
import com.trainingproject.habittrackerapp.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCurrentUserUseCaseTest {
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var mockAuthRepository: AuthRepository

    @Before
    fun setUp() {
        mockAuthRepository = mock()
        getCurrentUserUseCase = GetCurrentUserUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke returns null when user is not logged in`() = runTest {
        whenever(mockAuthRepository.getCurrentUser()).thenReturn(flowOf(null))

        val emittedUser = getCurrentUserUseCase().single()

        Assert.assertNull(emittedUser)
    }

    @Test
    fun `invoke returns current user from repository`() = runTest {
        val expectedUser = User(
            id = "UserId"
        )

        whenever(mockAuthRepository.getCurrentUser()).thenReturn(flowOf(expectedUser))

        val emittedUser = getCurrentUserUseCase().single()

        Assert.assertEquals(emittedUser, expectedUser)
    }
}