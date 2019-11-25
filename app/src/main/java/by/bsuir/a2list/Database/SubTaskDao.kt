package by.bsuir.a2list.Database

import androidx.room.*

@Dao
interface SubTaskDao {
    @Query("SELECT * FROM SubTask WHERE original_task = :id")
    fun getAll(id: Int): MutableList<SubTask>

    @Insert
    fun insertAll(vararg subtasks: SubTask)

    @Update
    fun updateTask(subtask: SubTask)

    @Query("DELETE FROM SubTask WHERE isDone = 1")
    fun deleteTasks()

    @Query("SELECT * FROM SubTask WHERE task = :task")
    fun selectItem(task: String): SubTask

    @Query("SELECT * FROM SubTask WHERE original_task = :id")
    fun selectItemByOriginalTask(id: Int): SubTask
}