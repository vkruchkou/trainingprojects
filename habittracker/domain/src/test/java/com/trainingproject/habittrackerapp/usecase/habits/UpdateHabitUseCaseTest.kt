package com.trainingproject.habittrackerapp.usecase.habits

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.usecase.habits.UpdateHabitUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class UpdateHabitUseCaseTest {

    private lateinit var updateHabitUseCase: UpdateHabitUseCase
    private val mockHabitRepository: HabitRepository = mock()

    @Before
    fun setUp() {
        updateHabitUseCase = UpdateHabitUseCase(mockHabitRepository)
    }

    @Test
    fun `invoke calls updateHabit on repository with correct habit`() = runTest {
        val habit = Habit(
            id = "habit1",
            userId = "user1",
            name = "Read",
            description = "Read 10 pages",
            goalDays = 30
        )
        updateHabitUseCase(habit)
        verify(mockHabitRepository).updateHabit(habit)
    }
}