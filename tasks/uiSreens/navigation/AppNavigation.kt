package com.trainingproject.tasks.uiSreens.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trainingproject.tasks.data.TaskDatabase
import com.trainingproject.tasks.data.TaskRepository
import com.trainingproject.tasks.uiSreens.screens.mainScreen.MainScreen
import com.trainingproject.tasks.uiSreens.screens.SettingsScreen
import com.trainingproject.tasks.viewmodel.TaskViewModelFactory
import com.trainingproject.tasks.uiSreens.screens.AddEditTaskScreen
import com.trainingproject.tasks.viewmodel.AddEditTaskViewModelFactory
import com.trainingproject.tasks.viewmodel.ClearAllTaskUseCase
import com.trainingproject.tasks.viewmodel.SettingsViewModel

@Composable
fun AppTasksNavigation(settingsVM: SettingsViewModel) {
    val context = LocalContext.current
    val dao = TaskDatabase.getInstance(context).taskDao()
    val repo = TaskRepository.getInstance(dao)

    val navController = rememberNavController()
    NavHost (navController = navController,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        startDestination = "main",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
        }) {

        composable("main") {
            MainScreen(
                vm = viewModel(
                    factory = TaskViewModelFactory(repo)
                ),
                onAddTaskClick = { navController.navigate("add") { launchSingleTop = true } },
                onSettingsClick = { navController.navigate("settings") { launchSingleTop = true } },
                onTaskCardClick = { task -> navController.navigate("details/${task.taskId}") { launchSingleTop = true } }
            )
        }
        composable("add") {
            AddEditTaskScreen(
                onBackClick = { navController.popBackStack() },
                vm = viewModel(
                    factory = AddEditTaskViewModelFactory(repo, null)
                )
            )
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                vm = settingsVM,
                clearAllTaskUseCase = ClearAllTaskUseCase(repo)
            )
        }

        composable("details/{taskId}",
            ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            AddEditTaskScreen(
                onBackClick = { navController.popBackStack() },
                vm = viewModel(
                    factory = AddEditTaskViewModelFactory(repo, taskId)
                )
            )
        }
    }
}