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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.trainingproject.tasks.viewmodel.AddEditTaskViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(vm: AddEditTaskViewModel, onBackClick: () -> Unit ) {
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
            val uiState = vm.uiState.collectAsState()

            AddEditTasksContent(
                modifier = Modifier.padding(paddingValues),
                taskName = uiState.value.taskName,
                onTaskNameChange = { str -> vm.setTaskName(str) },
                description = uiState.value.description,
                onDescriptionChange = { str -> vm.setDescription(str) },
                dueDate = uiState.value.dueDate,
                setDueDate = { date -> vm.setDueDate(date) },
                addOrUpdate = uiState.value.addOrEdit,
                addOrUpdateTask =  { vm.addOrUpdate() },
                onBackClick = onBackClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTasksContent(modifier: Modifier,
                        taskName: String,
                        onTaskNameChange: (String) -> Unit,
                        description: String,
                        onDescriptionChange: (String) -> Unit,
                        dueDate: Long?,
                        setDueDate: (Long?) -> Unit,
                        addOrUpdate: Boolean,
                        addOrUpdateTask: () -> Unit,
                        onBackClick: () -> Unit){
    val context = LocalContext.current

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

    val onDismiss = { setDueDate(null) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = taskName,
            onValueChange = onTaskNameChange,
            label = { Text("Task Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
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
            Text(dueDate?.let { dateFormatterWithTime.format(Date(it)) } ?: "Due date (not necessary)")
        }

        if (showDatePicker.value) {
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = {
                        setDueDate(datePickerState.selectedDateMillis)
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
                        setDueDate(
                            dueDate?.let { current ->
                                current +
                                        timePickerState.hour * 60 * 60 * 1000 +
                                        timePickerState.minute * 60 * 1000
                            }
                        )
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
                if (taskName.isBlank()) {
                    Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                keyboardController?.hide()
                addOrUpdateTask()
                Toast.makeText(context, if (addOrUpdate) "Task added" else "Task updated", Toast.LENGTH_SHORT).show()
                onBackClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (addOrUpdate) "Save Task" else "Update Task")
        }
    }
}