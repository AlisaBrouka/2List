package by.bsuir.a2list

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.room.Room
import by.bsuir.a2list.Database.Database
import by.bsuir.a2list.Database.SubTask
import by.bsuir.a2list.Database.Task
import kotlinx.android.synthetic.main.fragment_daytask.*
import java.util.*
import kotlin.collections.ArrayList

/*this is the fragment that opens when we click on the day and display the (sub)tasks if something was planned for that day*/

class DayTaskFragment : androidx.fragment.app.Fragment(){

    private var tasks = HashMap<Int, String>()
    private var tasksSubTasks = HashMap<String, MutableList<String>>()
    private var onlyTasks = mutableListOf<String>()
    private lateinit var adapter: CustomAdapter
    private var isChecked = ArrayList<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /*drawing a view*/
        val view: View = inflater.inflate(R.layout.fragment_daytask, container, false)
        val outerList: ListView = view.findViewById(R.id.outerList)
        val layout = view.findViewById<LinearLayout>(R.id.fragmentLayout)
        layout.removeView(outerList)
        layout.addView(outerList)

        /*open database*/
        val db = Room.databaseBuilder(
            activity!!.applicationContext,
            Database::class.java, "TasksDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        /*get info about the date for which tasks is going to be displayed, get tasks for db*/
        val currentDate = arguments!!.getString("date")
        val listTask: List<Task> = db.taskDao().getAll(currentDate)

        /*Log.i("1", listTask.size.toString() + " in database")

        //for debugging
        listTask.forEach {
            Log.i("id", "${it.id}")
            Log.i("id", "${it.task}")
            Log.i("id", "${it.date}")
            Log.i("id", "${it.reminder_time}")
            Log.i("id", "${it.isDone}")
        }

        listTask.forEach {
            val listSubTask: List<SubTask> = db.subTaskDao().getAll(it.id)
            listSubTask.forEach {
                Log.i("id", "${it.id}")
                Log.i("id", "${it.id_original}")
                Log.i("id", "${it.task}")
                Log.i("id", "${it.reminder_time}")
                Log.i("id", "${it.isDone}")
            }
        }*/

        /*get full info about every task and put into tasks list only info to display*/
        var taskInfo: List<Task> = db.taskDao().getAll(currentDate)
        taskInfo.forEach {
            tasks.put(it.id, it.task)
        }

        /*get all subtasks for all tasks in one list*/
        var subTasks: MutableList<SubTask> = arrayListOf()
        taskInfo.forEach {
            val tmp: MutableList<SubTask> = db.subTaskDao().getAll(it.id)
            subTasks.addAll(tmp)
        }

        /*create map of "task - list of subtasks"*/
        tasks.forEach {
            var list = mutableListOf<String>()
            var id = it.key

            subTasks.forEach {
                if (it.id_original == id)
                    list.add(it.task)
            }

            onlyTasks.add(it.value)
            tasksSubTasks.put(it.value, list.toMutableList())
            list.clear()


            /*tasks.forEach {
                Log.i("id", "${it.key}")
                Log.i("task", "${it.value}")
            }*/
        }

        /*displaying list of tasks*/
        val isCheckedElements: ArrayList<Int> = arrayListOf(-1)
        val context: Context = activity!!.applicationContext
        adapter = CustomAdapter(context, onlyTasks, tasksSubTasks, isChecked, isCheckedElements)
        outerList.divider = null
        outerList.adapter = adapter

        /**/
        outerList.setOnItemClickListener{parent, view, position, id ->

            val isCheckedInner: ArrayList<Int>
            if(!isChecked.contains(position))
                isChecked.add(position)
            else isChecked.remove(position)
            isChecked.forEach{
                Log.i("currently in list", it.toString())
            }
            Log.i("1", "___")

            /*measure and sets the right size with considering all subtasks lists sizes*/
            var realListSize = tasks.size
            tasksSubTasks.forEach{
                realListSize += it.value.size
            }
            ++realListSize
            Log.i("1", realListSize.toString())
            val params = view.getLayoutParams()
            params.height = realListSize * 50

            /*redraw list when any subtask is (un)tagged as done*/
            outerList.layoutParams = params
            isCheckedInner = adapter.getInnerChecked()
            adapter = CustomAdapter(activity!!.applicationContext, onlyTasks, tasksSubTasks, isChecked, isCheckedInner)
            outerList.adapter = adapter

        }

        db.close()

        return view
    }
}