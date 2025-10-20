package com.trainingproject.tasks.uiSreens.screens.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import com.trainingproject.tasks.data.Task
import com.trainingproject.tasks.data.TaskDao
import com.trainingproject.tasks.data.TaskRepository
import com.trainingproject.tasks.viewmodel.TaskViewModel
import com.trainingproject.tasks.ui.theme.TasksTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: TaskViewModel, onAddTaskClick: () -> Unit, onSettingsClick: () -> Unit, onTaskCardClick: (Task) -> Unit ) {

    val listState = rememberSaveable(saver = LazyListState.Saver ) { LazyListState() }
    val showScrollToTop = listState.firstVisibleItemIndex > 5

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Tasks") },
                    actions = {
                        IconButton(onClick = { onSettingsClick() }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        },
        floatingActionButton = {
            Column {
                AnimatedVisibility(visible = showScrollToTop) {
                    val coroutineScope = rememberCoroutineScope()

                    SmallFloatingActionButton(
                        onClick = { coroutineScope.launch {
                            listState.scrollToItem(0)
                        } },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
                    }
                }

                FloatingActionButton(onClick = { onAddTaskClick() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { paddingValues ->
        val uiState = vm.uiState.collectAsState()

        TasksContent(tasks = uiState.value.items,
            setFilterType = { filter -> vm.setFilterType(filter) },
            setSortType = { sort -> vm.setSortType(sort) },
            currentFilterType = uiState.value.filterType,
            listState = listState,
            onTaskCardClick = onTaskCardClick,
            onToggleDone = { task -> vm.toggleDone(task) },
            onRemove = { task -> vm.deleteTask(task) },
            modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun TasksContent(tasks: List<Task>,
                 currentFilterType: TaskFilter,
                 setFilterType: (TaskFilter) -> Unit,
                 setSortType: (TaskSort) -> Unit,
                 listState: LazyListState,
                 modifier: Modifier,
                 onTaskCardClick: (Task) -> Unit,
                 onToggleDone: (Task) -> Unit = {},
                 onRemove: (Task) -> Unit = {}
){
    Column(modifier.padding(16.dp)) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                OutlinedButton(
                    onClick = { setFilterType(TaskFilter.ALL) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        if (currentFilterType == TaskFilter.ALL) Color.Gray.copy(alpha = 0.2f) else Color.Transparent
                    )
                ) { Text("All") }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { setFilterType(TaskFilter.ACTIVE) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        if (currentFilterType == TaskFilter.ACTIVE) Color.Gray.copy(alpha = 0.2f) else Color.Transparent
                    )
                ) { Text("Active") }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { setFilterType(TaskFilter.COMPLETED) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        if (currentFilterType == TaskFilter.COMPLETED) Color.Gray.copy(alpha = 0.2f) else Color.Transparent
                    )
                ) { Text("Completed") }
            }
            val isSortMenuExpanded = rememberSaveable { mutableStateOf(false) }

            Box {
                IconButton(
                    onClick = { isSortMenuExpanded.value = true },
                ) {
                    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                }
                DropdownMenu(
                    expanded = isSortMenuExpanded.value,
                    onDismissRequest = { isSortMenuExpanded.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("By Title") },
                        onClick = {
                            setSortType(TaskSort.TITLE)
                            isSortMenuExpanded.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("By Due Date") },
                        onClick = {
                            setSortType(TaskSort.DUE_DATE)
                            isSortMenuExpanded.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("By Created At") },
                        onClick = {
                            setSortType(TaskSort.CREATED_AT)
                            isSortMenuExpanded.value = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))


        Text(
            text = "Count: ${tasks.size}",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        if (tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tasks",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        else{
            LazyColumn( modifier = Modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(bottom = 30.dp)) {
                items(
                    count = tasks.size,
                    key = { index -> tasks[index].taskId }
                ) { index ->
                    val task = tasks[index]
                    TaskCard(
                        task = task,
                        onClick = { onTaskCardClick(task) },
                        onToggleDone = onToggleDone,
                        onRemove = onRemove
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    val sampleTasks = listOf(
        Task(
            taskId = 1,
            taskName = "Купить продукты",
            description = "Хлеб, молоко, яйца",
            taskDone = false,
            createdAt = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 3600_000
        ),
        Task(
            taskId = 2,
            taskName = "Написать отчёт",
            description = "Проект Android",
            taskDone = true,
            createdAt = System.currentTimeMillis() - 3600_000
        ),
        Task(
            taskId = 3,
            taskName = "Позвонить другу",
            description = "",
            taskDone = false,
            createdAt = System.currentTimeMillis() - 7200_000
        )
    )

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

    val previewVM = object : TaskViewModel(TaskRepository(FakeTaskDao(sampleTasks)),
        SavedStateHandle()
    ) {}

    TasksTheme {
        MainScreen(vm = previewVM,{},{}, {})
    }
}