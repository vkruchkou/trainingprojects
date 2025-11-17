package com.trainingproject.habittrackerapp.usecase.habits

import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.usecase.habits.DeleteHabitUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify

class DeleteHabitUseCaseTest {

    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private val mockHabitRepository: HabitRepository = Mockito.mock()

    @Before
    fun setUp() {
        deleteHabitUseCase = DeleteHabitUseCase(mockHabitRepository)
    }

    @Test
    fun `invoke calls deleteHabit on repository with correct habitId`() = runTest {
        val habitId = "habit"
        deleteHabitUseCase(habitId)
        verify(mockHabitRepository).deleteHabit(habitId)
    }
}