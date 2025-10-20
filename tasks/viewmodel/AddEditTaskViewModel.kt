package com.trainingproject.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingproject.tasks.data.Task
import com.trainingproject.tasks.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditTaskUiState(
    val addOrEdit: Boolean = true, //true if add, false if edit
    val taskName: String = "",
    val description: String = "",
    val dueDate: Long? = null
)

class AddEditTaskViewModel(
    private val repo: TaskRepository,
    private val taskId: Long?
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTaskUiState())

    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()
   // private val taskId: Long? = savedStateHandle["taskId"]

    init {
        if (taskId != null)
            viewModelScope.launch {
                loadTask(taskId)?.let {
                        task ->
                    _uiState.update {
                            ui ->
                        ui.copy(
                            addOrEdit = false,
                            taskName = task.taskName,
                            description = task.description,
                            dueDate = task.dueDate
                        )
                    }
                }
            }
    }

    suspend fun loadTask(taskId: Long): Task? {
        return repo.get(taskId)
    }

    private fun addTask(task: Task) {
        viewModelScope.launch { repo.insert(task) }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch { repo.update(task) }
    }

    fun addOrUpdate(){
        val task = Task(taskId = taskId ?: 0L,
            taskName = _uiState.value.taskName,
            description = _uiState.value.description,
            dueDate = _uiState.value.dueDate)
        if(_uiState.value.addOrEdit)
            addTask(task)
        else
            updateTask(task)
    }

    fun setDueDate(date: Long?){
        _uiState.update {
            it.copy(dueDate = date)
        }
    }

    fun setTaskName(name: String){
        _uiState.update {
            it.copy(taskName = name)
        }
    }

    fun setDescription(description: String){
        _uiState.update {
            it.copy(description = description)
        }
    }
}