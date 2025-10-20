package com.trainingproject.tasks.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

enum class ThemeMode(val displayName: String) {
    SYSTEM("System default"),
    LIGHT("Light"),
    DARK("Dark")
}

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val THEME_KEY = "theme_mode"
    }

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var themeMode = mutableStateOf(loadTheme())
        private set

    fun changeTheme(mode: ThemeMode) {
        themeMode.value = mode
        saveTheme(mode)
    }

    private fun saveTheme(mode: ThemeMode) {
        prefs.edit {
            putString(THEME_KEY, mode.name)
        }
    }

    private fun loadTheme(): ThemeMode {
        val saved = prefs.getString(THEME_KEY, ThemeMode.SYSTEM.name)
        return ThemeMode.valueOf(saved ?: ThemeMode.SYSTEM.name)
    }
}