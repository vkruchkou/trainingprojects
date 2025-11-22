package com.trainingproject.habittrackerapp.presentation.navigation

import com.trainingproject.habittrackerapp.presentation.habitdetail.HabitDetailScreen
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trainingproject.habittrackerapp.presentation.add_habit.AddHabitScreen
import com.trainingproject.habittrackerapp.presentation.auth.AuthScreen
import com.trainingproject.habittrackerapp.presentation.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        startDestination = Screen.HomeScreen.route) {
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.AddHabitScreen.route) {
            AddHabitScreen(navController = navController)
        }
        composable(Screen.HabitDetailScreen.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")
            HabitDetailScreen(navController = navController)
        }
    }
}
