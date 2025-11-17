package com.trainingproject.habittrackerapp.usecase.habits

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.usecase.habits.AddHabitUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlinx.coroutines.test.runTest

class AddHabitUseCaseTest {

    private lateinit var addHabitUseCase: AddHabitUseCase
    private val mockHabitRepository: HabitRepository = mock()

    @Before
    fun setUp() {
        addHabitUseCase = AddHabitUseCase(mockHabitRepository)
    }

    @Test
    fun `invoke calls addHabit on repository with correct habit`() = runTest {
        val habit = Habit(
            id = "habit1",
            userId = "user1",
            name = "Read",
            description = "Read 10 pages",
            goalDays = 30
        )
        addHabitUseCase(habit)
        verify(mockHabitRepository).addHabit(habit)
    }
}