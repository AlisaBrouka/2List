package by.bsuir.a2list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.room.Room
import by.bsuir.a2list.Database.Database
import by.bsuir.a2list.Database.SubTask
import by.bsuir.a2list.Database.Task

class MainActivity : AppCompatActivity() {

    /*bottom navigation menu*/
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                Toast.makeText(this, R.string.title_home, Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                Toast.makeText(this, R.string.title_dashboard, Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                Toast.makeText(this, R.string.title_notifications, Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        /*create main activity view*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*activate bottom menu*/
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        /*open database connection*/
        val db = Room.databaseBuilder(
            this,
            Database::class.java, "TasksDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        /*user can choose if he wants to clear memory by automatically removing all the tasks tagged as done in settings*/
        //get settings status from memory
        val prefs = getSharedPreferences("onOfButton", MODE_PRIVATE)
        val textFromPreferences: Boolean = prefs.getBoolean("isChecked", true)
        Log.i("isChecked now", textFromPreferences.toString())
        //if user choose to remove it, the tasks and its subtasks getting removed from db
        if (textFromPreferences){
            db.taskDao().deleteTasks()
            db.subTaskDao().deleteTasks()
        }
        db.close()

        /*open main calendar activity*/
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)

        /*just some sample data for debugging*/
        //dbInitialise()
    }

    private fun dbInitialise() {

        /*open db*/
        val db = Room.databaseBuilder(
            this,
            Database::class.java, "TasksDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        /*month numbers are 0-11*/
        db.taskDao().insertAll(
            Task(task = "Подготовка к Рождеству", date = "2019-11-24"),
            /*reminder is not released in this version*/
            Task(task = "Отправить посылку", date = "2019-11-24", reminder_time = "2019-11-23-18-00"),
            Task(task = "Сходить в гости", date = "2019-11-25")
        )

        //change db version before compile or tasks created above id's would be higher than 1,2,3..
        //with current basic setting you won't be able to see the second subtask, as it is tagged as done and getting autoremoved when app is launching
        db.subTaskDao().insertAll(
            SubTask(id_original = 1, task = "Купить подарки"),
            SubTask(id_original = 1, task = "Украсить ёлку", isDone = true)
        )

        db.close()
    }
}
