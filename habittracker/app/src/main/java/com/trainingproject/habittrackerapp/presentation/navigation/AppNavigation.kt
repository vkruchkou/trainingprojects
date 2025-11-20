package com.trainingproject.habittrackerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trainingproject.habittrackerapp.presentation.auth.AuthScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.AuthScreen.route) {
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController = navController)
        }
//        composable(Screen.HomeScreen.route) {
//            HomeScreen(navController = navController)
//        }
//        composable(Screen.AddHabitScreen.route) {
//            AddHabitScreen(navController = navController)
//        }
//        composable(Screen.HabitDetailScreen.route) { backStackEntry ->
//            val habitId = backStackEntry.arguments?.getString("habitId")
//            HabitDetailScreen(navController = navController)
//        }
//        composable(Screen.StatsScreen.route) {
//            StatsScreen(navController = navController)
//        }
    }
}
