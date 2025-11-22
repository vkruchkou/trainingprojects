package com.trainingproject.habittrackerapp.presentation.add_habit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.trainingproject.habittrackerapp.presentation.navigation.Screen
import com.trainingproject.habittrackerapp.presentation.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    viewModel: AddHabitViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.event) {
        val event = state.event ?: return@LaunchedEffect

        when (event) {
            is UiEvent.Navigate -> {
                navController.navigate(event.route)
            }

            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action
                )
            }

            UiEvent.PopBackStack -> navController.popBackStack()
        }
        viewModel.onEventConsumed()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Habit") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        AddHabitContent(
            state = state,
            paddingValues = paddingValues,
            onNameChange = viewModel::onNameChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onIconUrlChange = viewModel::onIconUrlChange,
            onGoalDaysChange = viewModel::onGoalDaysChange,
            onSaveHabit = viewModel::onSaveHabit
        )
    }
}

@Composable
fun AddHabitContent(
    state: AddHabitUiState,
    paddingValues: PaddingValues,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIconUrlChange: (String) -> Unit,
    onGoalDaysChange: (String) -> Unit,
    onSaveHabit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.iconUrl,
            onValueChange = onIconUrlChange,
            label = { Text("Icon URL (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.goalDays,
            onValueChange = onGoalDaysChange,
            label = { Text("Goal Days (e.g., 21)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSaveHabit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Create Habit")
            }
        }
    }
}