package com.trainingproject.habittrackerapp.domain.repository

import com.trainingproject.habittrackerapp.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits(userId: String): Flow<List<Habit>>
    suspend fun getHabitById(habitId: String): Habit?
    suspend fun addHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: String)
}