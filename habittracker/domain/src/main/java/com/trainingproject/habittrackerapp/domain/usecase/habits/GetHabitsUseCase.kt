package com.trainingproject.habittrackerapp.domain.usecase.habits

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(userId: String): Flow<List<Habit>> {
        return habitRepository.getHabits(userId)
    }
}