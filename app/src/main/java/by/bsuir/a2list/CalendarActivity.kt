package by.bsuir.a2list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.View
import android.widget.TableRow
import android.widget.Toast
import androidx.room.Room
import by.bsuir.a2list.Database.Database
import by.bsuir.a2list.Database.SubTask
import by.bsuir.a2list.Database.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.text.SimpleDateFormat


class CalendarActivity : AppCompatActivity(), View.OnClickListener {

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

    /*used for calendar displaying*/
    private val rightNow: Calendar = Calendar.getInstance()

    private val weeksFragmentsList: List<Int> = listOf(
        R.id.week1Fragment,
        R.id.week2Fragment,
        R.id.week3Fragment,
        R.id.week4Fragment,
        R.id.week5Fragment,
        R.id.week6Fragment
    )

    private val weeksTitlesList: List<Int> = listOf(
        R.id.monday,
        R.id.tuesday,
        R.id.wednesday,
        R.id.thursday,
        R.id.friday,
        R.id.saturday,
        R.id.sunday
    )

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

    private val numbersOfWeeksList: List<String> = listOf(
        "1st",
        "2nd",
        "3rd",
        "4th",
        "5th",
        "6th"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        /*creating a view*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        /*calendar element looks like number in a square, function for adaptive making this squares depending on display size*/
        makeSquares()

        /*adding info about dates and days of month in calendar, 0 means we start from current month*/
        setMonthDates(0)

        /*info about days' tasks being displayed in fragment, all of them a hidden at the beginning*/
        hideAllFragments()

        /*adding bottom menu*/
        val navView: BottomNavigationView = findViewById(R.id.bottom_menu)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onClick (v: View){

        /*I use table layout, where all info about one week belongs to one row*/

        when (v.id){
            R.id.buttonPreviousMonth -> {
                //do not display any info about any tasks of any day
                hideAllFragments()
                //numberOfPreviousMonth = numberOfCurrentMonth - 1
                setMonthDates(-1)
            }
            R.id.buttonNextMonth -> {
                //do not display any info about any tasks of any day
                hideAllFragments()
                //numberOfNextMonth = numberOfCurrentMonth + 1
                setMonthDates(1)
            }
            //click at any free place means "do not display any info about any tasks of any day"
            else -> hideAllFragments()
        }

        allWeeksList.forEach {
            /*produces click at the day of the month*/
            if(v.id in it){
                displayFragment(allWeeksList.indexOf(it), v.id)
            }
        }
    }

    private fun displayFragment(n: Int, id: Int){

        /*if no info about tasks from any day of this week is being displayed and fragment for tasks is hidden, that open it and show tasks*/
        if (findViewById<TableRow>(weeksFragmentsList[n]).visibility == View.GONE) {
            //hide all previous info
            hideAllFragments()

            //get chosen date
            val textView: TextView = findViewById(id)
            val date = rightNow.get(Calendar.YEAR).toString() + "-" +  rightNow.get(Calendar.MONTH).toString() + "-" + textView.text.toString()

            //save info about date
            val bundle: Bundle = Bundle()
            bundle.putString("date", date)

            //start fragment with info about date
            val fragment = DayTaskFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(weeksFragmentsList[n], fragment).commit()

            //make fragment with tasks of the day visible
            findViewById<TableRow>(weeksFragmentsList[n]).visibility = View.VISIBLE
        }
        //if something about tasks of any day at this week is being displayed, hide it
        else if (findViewById<TableRow>(weeksFragmentsList[n]).visibility == View.VISIBLE)
            findViewById<TableRow>(weeksFragmentsList[n]).visibility = View.GONE

        //show info to the user that it is worked
        val numberOfWeekStr: String = numbersOfWeeksList[n]
        Toast.makeText(this, "$numberOfWeekStr week element clicked", Toast.LENGTH_SHORT).show()
    }

    private fun hideAllFragments(){

        weeksFragmentsList.forEach{
            findViewById<TableRow>(it).visibility = View.GONE
        }
    }

    private fun makeSquares(){

        weeksTitlesList.forEach {
            findViewById<TextView>(it).postDelayed(Runnable {

                    findViewById<TextView>(it).invalidate()
                    findViewById<TextView>(it).height = (findViewById<TextView>(it).width / 2)
                }, 1)
        }

        allWeeksList.forEach {
            it.forEach {
                findViewById<TextView>(it).postDelayed(Runnable {

                    findViewById<TextView>(it).invalidate()
                    findViewById<TextView>(it).height = findViewById<TextView>(it).width
                }, 1)
            }
        }
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
