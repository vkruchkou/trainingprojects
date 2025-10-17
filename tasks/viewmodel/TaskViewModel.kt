package com.trainingproject.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.tasks.data.Task
import com.trainingproject.tasks.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    open val tasks: StateFlow<List<Task>> = repo.getAll()
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())
    open val activeTasks: StateFlow<List<Task>> = repo.getActive()
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())
    open val completedTasks: StateFlow<List<Task>> = repo.getCompleted()
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

    fun getTask(taskId: Long): StateFlow<Task?> {
        return repo.get(taskId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = null)
    }

    fun addTask(task: Task) {
        viewModelScope.launch { repo.insert(task) }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch { repo.update(task) }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch { repo.update(task.copy(taskDone = !task.taskDone)) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repo.delete(task) }
    }

    fun clearAll() {
        viewModelScope.launch { repo.clearAll() }
    }
}