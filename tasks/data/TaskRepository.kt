package com.trainingproject.tasks.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    fun get(taskId: Long): Flow<Task?> = dao.get(taskId)
    fun getAll(): Flow<List<Task>> = dao.getAll()
    fun getActive(): Flow<List<Task>> = dao.getActive()
    fun getCompleted(): Flow<List<Task>> = dao.getCompleted()

    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun clearAll() = dao.clearAll()
}