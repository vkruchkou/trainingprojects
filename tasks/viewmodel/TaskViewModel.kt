package com.trainingproject.tasks.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.tasks.data.Task
import com.trainingproject.tasks.data.TaskRepository
import com.trainingproject.tasks.uiSreens.screens.mainScreen.TaskFilter
import com.trainingproject.tasks.uiSreens.screens.mainScreen.TaskSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val FILTER_TYPE_SAVED_STATE_KEY = "FILTER_TYPE_SAVED_STATE_KEY"

const val SORT_TYPE_SAVED_STATE_KEY = "SORT_TYPE_SAVED_STATE_KEY"
data class TaskUiState(
    val items: List<Task> = emptyList(),
    val filterType: TaskFilter = TaskFilter.ALL
)
open class TaskViewModel(
    private val repo: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedFilterType = savedStateHandle.getStateFlow(FILTER_TYPE_SAVED_STATE_KEY, TaskFilter.ALL)
    private val _savedSortType = savedStateHandle.getStateFlow(SORT_TYPE_SAVED_STATE_KEY, TaskSort.CREATED_AT)
    private val _tasks: Flow<List<Task>> = repo.getAll()

    val uiState: StateFlow<TaskUiState> = combine(
        _tasks,
        _savedFilterType,
        _savedSortType
    ) { tasks, filter, sort ->
        TaskUiState( items = filterTasks(tasks, sort, filter), filter )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), TaskUiState())

    fun setFilterType(requestType: TaskFilter) {
        savedStateHandle[FILTER_TYPE_SAVED_STATE_KEY] = requestType
    }

    fun setSortType(requestType: TaskSort) {
        savedStateHandle[SORT_TYPE_SAVED_STATE_KEY] = requestType
    }

    private fun filterTasks(tasks: List<Task>, sort: TaskSort, filter: TaskFilter): List<Task>{
        val filteredTasks =
            when (filter) {
                TaskFilter.ALL -> tasks
                TaskFilter.ACTIVE -> tasks.filter { !it.taskDone }
                TaskFilter.COMPLETED -> tasks.filter { it.taskDone }
            }

        return when (sort) {
            TaskSort.TITLE -> filteredTasks.sortedBy { it.taskName }
            TaskSort.DUE_DATE -> filteredTasks.sortedBy { it.dueDate }
            TaskSort.CREATED_AT -> filteredTasks.sortedByDescending { it.createdAt }
        }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch { repo.update(task.copy(taskDone = !task.taskDone)) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repo.delete(task) }
    }
}