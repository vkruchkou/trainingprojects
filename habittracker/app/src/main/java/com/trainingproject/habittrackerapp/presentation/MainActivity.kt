package com.trainingproject.habittrackerapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.trainingproject.habittrackerapp.presentation.navigation.AppNavigation
import com.trainingproject.habittrackerapp.ui.theme.HabitTrackerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerAppTheme {
                AppNavigation()
            }
        }
    }
}