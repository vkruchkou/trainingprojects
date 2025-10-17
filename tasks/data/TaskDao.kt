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
    fun get(taskId: Long): Flow<Task?>

    @Query("SELECT * FROM task_table ORDER BY created_at DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_done = 0 ORDER BY created_at DESC")
    fun getActive(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_done = 1 ORDER BY created_at DESC")
    fun getCompleted(): Flow<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun clearAll()
}