package by.bsuir.a2list.Database

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE date = :date")
    fun getAll(date: String): MutableList<Task>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM Task WHERE isDone = 1")
    fun deleteTasks()

    @Query("SELECT * FROM TASK WHERE task = :task")
    fun selectItem(task: String): Task
}