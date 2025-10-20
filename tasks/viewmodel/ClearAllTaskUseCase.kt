package com.trainingproject.tasks.viewmodel

import com.trainingproject.tasks.data.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClearAllTaskUseCase(private val repo: TaskRepository) {
    fun clearAll() {
        CoroutineScope(Dispatchers.IO).launch { repo.clearAll() }
    }
}