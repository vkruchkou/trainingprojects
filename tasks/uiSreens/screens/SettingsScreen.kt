package com.trainingproject.tasks.uiSreens.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trainingproject.tasks.data.Task
import com.trainingproject.tasks.data.TaskDao
import com.trainingproject.tasks.data.TaskRepository
import com.trainingproject.tasks.viewmodel.ClearAllTaskUseCase
import com.trainingproject.tasks.viewmodel.SettingsViewModel
import com.trainingproject.tasks.viewmodel.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    vm: SettingsViewModel,
    clearAllTaskUseCase: ClearAllTaskUseCase
) {
    val context = LocalContext.current
    val currentTheme = vm.themeMode

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme.value == mode,
                            onClick = { vm.changeTheme(mode) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(mode.displayName)
                    }
                }
            }

            HorizontalDivider()

            Button(
                onClick = {
                    clearAllTaskUseCase.clearAll()
                    Toast.makeText(context, "All tasks deleted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Clear all tasks")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    class FakeTaskDao(private val tasks: List<Task>) : TaskDao {
        override fun get(taskId: Long): Flow<Task> =
            flowOf(tasks.filter { it.taskId == taskId }[0])

        override fun getAll(): Flow<List<Task>> = flowOf(tasks)
        override suspend fun clearAll() {}

        override suspend fun insert(task: Task) {}
        override suspend fun update(task: Task) {}
        override suspend fun delete(task: Task) {}
        override suspend fun insertAll(tasks: List<Task>) {}
    }
    SettingsScreen(
        onBackClick = {},
        vm = viewModel(),
        clearAllTaskUseCase= ClearAllTaskUseCase(TaskRepository(FakeTaskDao(emptyList())))
    )
}