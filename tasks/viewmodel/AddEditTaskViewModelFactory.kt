package com.trainingproject.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingproject.tasks.data.TaskRepository

class AddEditTaskViewModelFactory(
    private val repo: TaskRepository,
    private val taskId: Long?
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AddEditTaskViewModel::class.java)) {
            return AddEditTaskViewModel(repo, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}