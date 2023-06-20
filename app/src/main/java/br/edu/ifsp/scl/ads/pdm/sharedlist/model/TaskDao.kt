package br.edu.ifsp.scl.ads.pdm.sharedlist.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun createTask(task: Task)

    @Query("SELECT * FROM Task WHERE id = :id")
    fun retrieveTask(id: Int): Task?

    @Query("SELECT * FROM Task")
    fun retrieveTasks(): MutableList<Task>

    @Query("SELECT COUNT(*) FROM Task WHERE titulo = :titulo")
    fun findTitle(titulo: String): Int

    @Update
    fun updateTask(task: Task): Int

    @Delete
    fun deleteTask(task: Task): Int
}