package com.trainingproject.habittrackerapp.domain.usecase.habits

import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        habitRepository.addHabit(habit)
    }
}