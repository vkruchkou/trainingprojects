package com.trainingproject.tasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Insert
    suspend fun insertAll(tasks: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("SELECT * FROM task_table ORDER BY created_at DESC")
    fun getAll(): Flow<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun clearAll()
}