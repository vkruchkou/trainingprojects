package com.trainingproject.habittrackerapp.usecase.habits

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import com.trainingproject.habittrackerapp.domain.usecase.habits.GetHabitsUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

class GetHabitsUseCaseTest {

    private lateinit var getHabitsUseCase: GetHabitsUseCase
    private lateinit var mockHabitRepository: HabitRepository

    @Before
    fun setUp() {
        mockHabitRepository = mock()
        getHabitsUseCase = GetHabitsUseCase(mockHabitRepository)
    }

    @Test
    fun `invoke returns flow of habits from repository`() = runTest {
        val userId = "user1"
        val habits = listOf(
            Habit(id = "h1", userId = userId, name = "Habit 1"),
            Habit(id = "h2", userId = userId, name = "Habit 2")
        )
        whenever(mockHabitRepository.getHabits(userId)).thenReturn(flowOf(habits))

        val emittedHabits = getHabitsUseCase(userId).single()

        Assert.assertEquals(habits, emittedHabits)
    }

    @Test
    fun `invoke returns empty flow when no habits are available`() = runTest {
        val userId = "user1"
        whenever(mockHabitRepository.getHabits(userId)).thenReturn(flowOf(emptyList()))

        val emittedHabits = getHabitsUseCase(userId).single()

        Assert.assertEquals(emptyList<Habit>(), emittedHabits)
    }
}