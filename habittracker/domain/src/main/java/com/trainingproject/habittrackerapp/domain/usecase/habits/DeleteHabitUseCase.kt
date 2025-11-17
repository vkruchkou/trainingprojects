package com.trainingproject.habittrackerapp.domain.usecase.habits

import com.trainingproject.habittrackerapp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String) {
        habitRepository.deleteHabit(habitId)
    }
}