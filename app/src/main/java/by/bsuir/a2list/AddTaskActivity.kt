package by.bsuir.a2list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import by.bsuir.a2list.Database.Database
import by.bsuir.a2list.Database.SubTask
import by.bsuir.a2list.Database.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTaskActivity: AppCompatActivity(), View.OnClickListener {

    /*bottom navigation menu*/
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val intent = Intent(this, AddTaskActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val rightNow: Calendar = Calendar.getInstance()
    lateinit var adapter: AddTaskAdapter

    var sublist: ArrayList<String>? = null
    lateinit var list: ListView
    lateinit var date: String

    private val week1List: List<Int> = listOf(
        R.id.week1Monday,
        R.id.week1Tuesday,
        R.id.week1Wednesday,
        R.id.week1Thursday,
        R.id.week1Friday,
        R.id.week1Saturday,
        R.id.week1Sunday
    )

    private val week2List: List<Int> = listOf(
        R.id.week2Monday,
        R.id.week2Tuesday,
        R.id.week2Wednesday,
        R.id.week2Thursday,
        R.id.week2Friday,
        R.id.week2Saturday,
        R.id.week2Sunday
    )

    private val week3List: List<Int> = listOf(
        R.id.week3Monday,
        R.id.week3Tuesday,
        R.id.week3Wednesday,
        R.id.week3Thursday,
        R.id.week3Friday,
        R.id.week3Saturday,
        R.id.week3Sunday
    )

    private val week4List: List<Int> = listOf(
        R.id.week4Monday,
        R.id.week4Tuesday,
        R.id.week4Wednesday,
        R.id.week4Thursday,
        R.id.week4Friday,
        R.id.week4Saturday,
        R.id.week4Sunday
    )

    private val week5List: List<Int> = listOf(
        R.id.week5Monday,
        R.id.week5Tuesday,
        R.id.week5Wednesday,
        R.id.week5Thursday,
        R.id.week5Friday,
        R.id.week5Saturday,
        R.id.week5Sunday
    )

    private val week6List: List<Int> = listOf(
        R.id.week6Monday,
        R.id.week6Tuesday,
        R.id.week6Wednesday,
        R.id.week6Thursday,
        R.id.week6Friday,
        R.id.week6Saturday,
        R.id.week6Sunday
    )

    private val allWeeksList: List<List<Int>> = listOf(
        week1List,
        week2List,
        week3List,
        week4List,
        week5List,
        week6List
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        /*open database connection*/
        val db = Room.databaseBuilder(
            this,
            Database::class.java, "TasksDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        /*creating the view*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        /*adding bottom navigation menu*/
        val navView: BottomNavigationView = findViewById(R.id.bottom_menu)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //for adding as much fields for subtasks as needed
        list = findViewById(R.id.listSubTask)
        adapter = AddTaskAdapter(this, sublist)
        list.adapter = adapter

        /*set date style displayed as shortcut before calendar is opened*/
        val setDate: Calendar = Calendar.getInstance()
        //MMM is for short month names: Sep, Nov and so on
        val monthDate = SimpleDateFormat("MMM")
        val monthName = monthDate.format(setDate.time)
        date = setDate.get(Calendar.YEAR).toString() + "-" + setDate.get(Calendar.MONTH) + "-" + setDate.get(Calendar.DATE)
        val stylizedDate = setDate.get(Calendar.YEAR).toString() + "-" + monthName + "-" + setDate.get(Calendar.DATE)
        val textView: TextView = findViewById(R.id.currentDate)
        textView.text = stylizedDate

        /*attach and hide calendar*/
        val calendar: RelativeLayout = findViewById(R.id.calendar)
        calendar.visibility = View.GONE

        setMonthDates(0)
    }

    override fun onClick(v: View) {

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "TasksDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        when(v.id) {
            //red cross button that adds another field to add a task
            R.id.addTask -> {
                /*val setDate: Calendar = Calendar.getInstance()
                val monthDate = SimpleDateFormat("MMMM")
                val monthName = monthDate.format(setDate.time)

                val textEdit: EditText = findViewById(R.id.addTask)
                val text = textEdit.text.toString()*/

                //read currently added subtask and add this to list
                val textSubTask: EditText? = findViewById(R.id.addSubTask)
                if (textSubTask != null) {
                    val subTask = textSubTask.text.toString()
                    sublist!!.add(subTask)
                }

                //dispalays subtasks
                if (sublist == null) sublist = arrayListOf("")
                adapter = AddTaskAdapter(this, sublist)
                list.adapter = adapter
            }

            //scroll throw months
            R.id.buttonPreviousMonth -> setMonthDates(-1)
            R.id.buttonNextMonth -> setMonthDates(1)

            //button that opens the calendar
            R.id.addDate -> {

                val calendar: RelativeLayout = findViewById(R.id.calendar)
                //that TextView displays current date (later - date that was chosen for the task)
                val currentDate: TextView = findViewById(R.id.currentDate)

                /*if you can ses calendar you cannot see current date field and vice versa*/
                if (calendar.visibility == View.GONE) {
                    calendar.visibility = View.VISIBLE
                    currentDate.visibility = View.GONE
                } else if (calendar.visibility == View.VISIBLE) {
                    calendar.visibility = View.GONE
                    currentDate.visibility = View.VISIBLE
                }
            }

            //button that saves added task and subtasks
            R.id.saveTasks -> {

                //subtask added to the list of subtasks
                val textSubTask: EditText? = findViewById(R.id.addSubTask)
                if (textSubTask != null) {
                    val subTask = textSubTask.text.toString()
                    sublist!!.add(subTask)
                }

                //task input check, read
                val textEdit: EditText = findViewById(R.id.addTask)
                val text = textEdit.text.toString()
                if(text == ""){
                    Toast.makeText(this, "Введите задачу", Toast.LENGTH_SHORT).show()
                    return
                }

                //task added to db
                if (text != "") {
                    db.taskDao().insertAll(Task(task = text, date = date))
                }
                val currentId: Int = db.taskDao().selectItem(text).id
                Log.i("task id", currentId.toString())


                //subtask added to db
                if (sublist != null ){
                    sublist!!.forEach {
                            if (it != ""){
                                db.subTaskDao().insertAll(SubTask(id_original = currentId, task = it))
                             }
                    }
                }

                db.close()
            }
        }

        allWeeksList.forEach {
            /*produces click at the day of the month*/
            if(v.id in it){
                date = getCurrentDate(v.id)
                val textView: TextView = findViewById(R.id.currentDate)
                textView.text = date
            }
        }

    }

    private fun getCurrentDate(id: Int): String{
        val currentDate: TextView = findViewById(id)
        val monthDate = SimpleDateFormat("MMM")
        val monthName = monthDate.format(rightNow.time)
        val date: String = rightNow.get(Calendar.YEAR).toString() + "-" + monthName + "-" + currentDate.text.toString()
        Toast.makeText(this, "$date was chosen", Toast.LENGTH_SHORT).show()
        return date
    }

    private fun setMonthDates(side: Int){

        /*making days' elements visible*/
        allWeeksList.forEach {
            it.forEach {
                findViewById<TextView>(it).visibility = View.VISIBLE
                findViewById<TextView>(it).isClickable = true
            }
        }

        /*side is the parameter we pass when we need to scroll to previous (-1) or next (+1) month.
        For current month when creating a calendar at the beginning we pass 0*/
        rightNow.add(Calendar.MONTH, side)
        rightNow.set(Calendar.DAY_OF_MONTH, 1)

        /*getting current day, display it*/
        val monthYear: TextView = findViewById(R.id.monthYear)
        //MMMM means full month name being displayed
        val monthDate = SimpleDateFormat("MMMM")
        val monthName = monthDate.format(rightNow.getTime())
        //display above calendar
        monthYear.text = monthName + " " + rightNow.get(Calendar.YEAR).toString()

        /*Not every month starts from Monday
        When our month starts from Friday, for example, the days in Monday-Thursday range belong to the first week should not be displayed, clicked
        */

        /* this is helper function
        Monday is the second day of week into this calendar, number 2 at the left side
        Monday is first day of week in our normal calendar, number 0 at right side as into list of weeks it is at 1st position with index 0
        */
        var firstDayOfMonth = when(rightNow.get(Calendar.DAY_OF_WEEK)){
            1 -> 6
            2 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5
            else -> -1
        }

        /*
        For all days at the first week that is before our first day of month
        */
        for(j in 0..firstDayOfMonth-1){

            findViewById<TextView>(week1List[j]).text = ""
            findViewById<TextView>(week1List[j]).isClickable = false
            //hide it
            findViewById<TextView>(week1List[j]).visibility = View.INVISIBLE
        }

        /*display the days of the week, add color point for the days with big number of tasks*/
        var j: Int = 0
        var dayOfWeek = firstDayOfMonth
        for(i in rightNow.getActualMinimum(Calendar.DAY_OF_MONTH)..rightNow.getActualMaximum(Calendar.DAY_OF_MONTH)){

            if(dayOfWeek == 7){
                dayOfWeek = 0
                j = ++j
            }

            rightNow.set(Calendar.DAY_OF_MONTH, i)

            /*allWeeksList[j] is list of id's for one week*/
            findViewById<TextView>(allWeeksList[j][dayOfWeek]).text = i.toString()

            dayOfWeek = ++dayOfWeek
        }

        /*hide icons for all the days of the last week(s) that are not from current month*/
        while( j != allWeeksList.size ){

            findViewById<TextView>(allWeeksList[j][dayOfWeek]).text = ""
            findViewById<TextView>(allWeeksList[j][dayOfWeek]).visibility = View.INVISIBLE

            dayOfWeek = ++dayOfWeek
            if(dayOfWeek == 7){
                dayOfWeek = 0
                j = ++j
            }
        }

    }

}