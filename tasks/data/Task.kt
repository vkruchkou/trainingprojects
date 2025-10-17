package com.trainingproject.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId:Long = 0L,

    @ColumnInfo(name="task_name")
    var taskName: String =" ",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "due_date")
    val dueDate: Long? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name="task_done")
    var taskDone: Boolean = false
)