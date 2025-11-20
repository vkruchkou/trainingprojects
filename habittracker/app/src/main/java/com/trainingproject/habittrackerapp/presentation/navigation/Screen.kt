package com.trainingproject.habittrackerapp.presentation.navigation

sealed class Screen(val route: String) {
    object AuthScreen : Screen("auth_screen")
    object HomeScreen : Screen("home_screen")
    object AddHabitScreen : Screen("add_habit_screen")
    object StatsScreen : Screen("stats_screen")
    object HabitDetailScreen : Screen("habit_detail_screen/{habitId}") {
        fun createRoute(habitId: String) = "habit_detail_screen/$habitId"
    }
}