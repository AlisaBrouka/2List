package by.bsuir.a2list

import by.bsuir.a2list.Database.Database
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.room.Room
import by.bsuir.a2list.Database.Task

/*this is the class with custom list of tasks adapter logic*/

class CustomAdapter
    (private val context: Context, private val tasks: MutableList<String>, private val dataSource: HashMap<String, MutableList<String>>, val isChecked: List<Int>, var isCheckedToo: ArrayList<Int>)
    : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //val isCheckedToo = ArrayList<Int>()

    /*opening database*/
    val db = Room.databaseBuilder(
        context,
        Database::class.java, "TasksDB"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    private lateinit var someParent: ViewGroup

    /*usual overriding functions*/
    override fun getCount(): Int {
       return tasks.size
    }

    override fun getItem(position: Int): Any {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        //val rowView = inflater.inflate(R.layout.custom_list_item, parent, false)
        val rowView = inflater.inflate(R.layout.custom_list_item, parent, false)
        someParent = parent

        /*dispalying a task*/
        val textView: TextView = rowView.findViewById(R.id.separateTaskText)
        textView.text = tasks[position]

        if(isChecked !=null && isChecked.contains(position)){
            val checkBox: CheckBox = rowView.findViewById(R.id.separateTaskCheckBox)
            checkBox.isChecked = true
            textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            val task = textView.text.toString()
            val taskFull = db.taskDao().selectItem(task)
            Log.i("from db", db.taskDao().selectItem(task).id.toString())
            db.taskDao().updateTask(Task(id = taskFull.id, task = taskFull.task, date = taskFull.date, reminder_time = taskFull.reminder_time, isDone = true))
        }

        /*creating inner list view*/
        val innerList: ListView = rowView.findViewById(R.id.innerList)
        innerList.visibility = View.VISIBLE
        innerList.divider = null

        //getting info about subtasks for the task
        val list: MutableList<String>? = dataSource[tasks[position]]
        //set the real size of list
        val params = innerList.getLayoutParams()
        params.height = list!!.size * 100
        innerList.layoutParams = params

        //show and add data to list of subtasks
        if (list != null){
            val adapter = InnerListAdapter(context, list, isCheckedToo)
            innerList.adapter = adapter
        }
        else innerList.visibility = View.GONE

        /*save info about subtasks marked as done*/
        innerList.setOnItemClickListener{parent, view, position, id ->
            Log.i("1", "clicked")
            if(!isCheckedToo.contains(position))
                isCheckedToo.add(position)
            else isCheckedToo.remove(position)

            //redraw the view every time the subtask is being (un)tagged
            val adapter = InnerListAdapter(context, list!!, isCheckedToo)
            innerList.adapter = adapter
        }

        /*code for redrawing the view every time task is being tagged*/
        val checkBox: CheckBox = rowView.findViewById(R.id.separateTaskCheckBox)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

            val textView: TextView = rowView.findViewById(R.id.separateTaskText)
            if(isChecked){
                //strikethrough text
                textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                val textView: TextView = rowView.findViewById(R.id.separateTaskText)

                //get info about task and update database
                val text = textView.text.toString()
                val task: Task = db.taskDao().selectItem(text)
                db.taskDao().updateTask(Task(id = task.id, task = task.task, date = task.date, reminder_time = task.reminder_time, isDone = true))
            }
            //remove strikethrough from text
            else textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        return rowView
    }

    fun getInnerChecked(): ArrayList<Int> {
        //info about subtasks being tagged as done
        return isCheckedToo
    }
}