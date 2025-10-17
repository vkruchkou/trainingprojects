package com.trainingproject.tasks.uiSreens.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trainingproject.tasks.uiSreens.screens.mainScreen.MainScreen
import com.trainingproject.tasks.uiSreens.screens.SettingsScreen
import com.trainingproject.tasks.viewmodel.TaskViewModel
import com.trainingproject.tasks.viewmodel.TaskViewModelFactory
import com.trainingproject.tasks.uiSreens.screens.AddEditTaskScreen
import com.trainingproject.tasks.viewmodel.SettingsViewModel

@Composable
fun AppTasksNavigation(settingsVM: SettingsViewModel) {
    val vm: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(LocalContext.current)
    )

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
                vm = vm,
                onAddTaskClick = { navController.navigate("add") { launchSingleTop = true } },
                onSettingsClick = { navController.navigate("settings") { launchSingleTop = true } },
                onTaskCardClick = { task -> navController.navigate("details/${task.taskId}") { launchSingleTop = true } }
            )
        }
        composable("add") {
            AddEditTaskScreen(addOrUpdateTask = { task -> vm.addTask(task) },
                onBackClick = { navController.popBackStack() })
        }

        composable("settings") {
            SettingsScreen(onBackClick = { navController.popBackStack() }, onClearAllTasks = { vm.clearAll() }, vm = settingsVM )
        }

        composable("details/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            taskId?.let {
                AddEditTaskScreen(
                    addOrUpdateTask = { task -> vm.updateTask(task) },
                    onBackClick = { navController.popBackStack() },
                    taskId = taskId,
                    getTask = { taskId -> vm.getTask(taskId) }
                ) } ?:  LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }
}