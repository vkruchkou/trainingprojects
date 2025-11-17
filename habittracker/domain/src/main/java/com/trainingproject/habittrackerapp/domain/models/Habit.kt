package com.trainingproject.habittrackerapp.domain.models

data class Habit(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val goalDays: Int = 21,
    val completedDates: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getProgress(): Int {
        return completedDates.size
    }

    fun isCompletedToday(): Boolean {
        val today = java.time.LocalDate.now().toString()
        return completedDates.contains(today)
    }

    fun completeToday(): Habit {
        val today = java.time.LocalDate.now().toString()
        return if (!completedDates.contains(today)) {
            copy(completedDates = completedDates + today)
        } else {
            this
        }
    }

    fun uncompleteToday(): Habit {
        val today = java.time.LocalDate.now().toString()
        return copy(completedDates = completedDates.filter { it != today })
    }
}