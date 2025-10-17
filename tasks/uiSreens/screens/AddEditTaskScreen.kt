package com.trainingproject.tasks.uiSreens.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.trainingproject.tasks.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(addOrUpdateTask: (Task) -> Unit, onBackClick: () -> Unit, taskId: Long = 0, getTask: (Long) -> StateFlow<Task?> = { MutableStateFlow(null) } ) {
    val context = LocalContext.current
    val task = if (taskId != 0L)
        getTask(taskId).collectAsState()
    else
        remember { mutableStateOf<Task?>(null) }
    val taskName = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val dueDate = rememberSaveable { mutableStateOf<Long?>(null) }

    task.value?.let {
        taskName.value = it.taskName
        description.value = it.description
        dueDate.value = it.dueDate
    }

    val now = Instant.ofEpochMilli(System.currentTimeMillis())
        .atZone(ZoneId.systemDefault())

    val dateFormatterWithTime = SimpleDateFormat("dd/MMM/yyyy, HH:mm", Locale.getDefault())
    dateFormatterWithTime.timeZone = TimeZone.getTimeZone("UTC")

    val timePickerState = rememberTimePickerState(
        initialHour = now.hour, initialMinute =  now.minute
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = now.toInstant().toEpochMilli()

    )
    val showTimePicker = rememberSaveable { mutableStateOf(false) }
    val showDatePicker = rememberSaveable { mutableStateOf(false) }

    val onDismiss = { dueDate.value = null }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Add Task") },
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
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = taskName.value,
                    onValueChange = { taskName.value = it },
                    label = { Text("Task Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { showDatePicker.value = true },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp)
                )
                {
                    Text(dueDate.value?.let { dateFormatterWithTime.format(Date(it)) } ?: "Due date (not necessary)")
                }

                if (showDatePicker.value) {
                    DatePickerDialog(
                        onDismissRequest = onDismiss,
                        confirmButton = {
                            TextButton(onClick = {
                                dueDate.value = datePickerState.selectedDateMillis
                                showTimePicker.value = true
                                showDatePicker.value = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                onDismiss()
                                showDatePicker.value = false
                            }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if(showTimePicker.value)
                {
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        dismissButton = {
                            TextButton(onClick = { onDismiss()
                                showTimePicker.value = false
                            }) {
                                Text("Cancel")
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                dueDate.value = dueDate.value?.let { current ->
                                current +
                                        timePickerState.hour * 60 * 60 * 1000 +
                                        timePickerState.minute * 60 * 1000
                                }
                                showTimePicker.value = false
                            }) {
                                Text("OK")
                            }
                        },
                        text = { TimePicker( state = timePickerState ) }
                    )
                }

                Button(
                    onClick = {
                        if (taskName.value.isBlank()) {
                            Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newTask = Task(
                            taskId = task.value?.taskId ?: 0L,
                            taskName = taskName.value,
                            description = description.value,
                            dueDate = dueDate.value,
                            taskDone = task.value?.taskDone ?: false

                        )
                        keyboardController?.hide()
                        addOrUpdateTask(newTask)
                        Toast.makeText(context, if (taskId == 0L) "Task added" else "Task updated", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (taskId == 0L) "Save Task" else "Update Task")
                }
            }
        }
    )
}