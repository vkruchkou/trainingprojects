package com.trainingproject.tasks.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingproject.tasks.data.TaskDatabase
import com.trainingproject.tasks.data.TaskRepository

class TaskViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
            val dao = TaskDatabase.Companion.getInstance(context).taskDao()
            val repo = TaskRepository(dao)
            return TaskViewModel(repo) as T
        }
        else throw IllegalArgumentException("Unknown ViewModel")
    }
}