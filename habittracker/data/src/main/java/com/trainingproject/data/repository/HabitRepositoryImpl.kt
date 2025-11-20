package com.trainingproject.data.repository

import com.trainingproject.data.local.FirebaseFirestoreSource
import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val firestoreSource: FirebaseFirestoreSource
) : HabitRepository {

    override fun getHabits(userId: String): Flow<List<Habit>> {
        return firestoreSource.getHabits(userId)
    }

    override suspend fun getHabitById(habitId: String): Habit? {
        return firestoreSource.getHabitById(habitId)
    }

    override suspend fun addHabit(habit: Habit) {
        firestoreSource.addHabit(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        firestoreSource.updateHabit(habit)
    }

    override suspend fun deleteHabit(habitId: String) {
        firestoreSource.deleteHabit(habitId)
    }
}
