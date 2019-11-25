package by.bsuir.a2list.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Task::class,
        SubTask::class
    ],
    version = 22, exportSchema = false)

abstract class Database :RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
}
