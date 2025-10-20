package com.trainingproject.tasks.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    suspend fun get(taskId: Long): Task? = dao.getTaskById(taskId)
    fun getAll(): Flow<List<Task>> = dao.getAll()
    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)
    suspend fun clearAll() = dao.clearAll()

    companion object {
        @Volatile
        private var INSTANCE: TaskRepository? = null

        fun getInstance(dao: TaskDao): TaskRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = TaskRepository(dao)
                INSTANCE = instance
                instance
            }
        }
    }
}