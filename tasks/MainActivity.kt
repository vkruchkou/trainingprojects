package com.trainingproject.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.trainingproject.tasks.ui.theme.TasksTheme
import com.trainingproject.tasks.uiSreens.navigation.AppTasksNavigation
import com.trainingproject.tasks.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode = settingsViewModel.themeMode

            TasksTheme(themeMode = themeMode.value) {
                AppTasksNavigation(settingsViewModel)
            }
        }
    }
}

