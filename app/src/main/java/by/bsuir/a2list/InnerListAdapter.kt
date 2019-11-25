package by.bsuir.a2list

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.room.Room
import by.bsuir.a2list.Database.Database
import by.bsuir.a2list.Database.SubTask
import by.bsuir.a2list.Database.Task

/*adapter for list of subtasks*/

class InnerListAdapter(context: Context, private val tasks: MutableList<String>, val isChecked: List<Int>): BaseAdapter()  {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /*open database connection*/
    val db = Room.databaseBuilder(
        context,
        Database::class.java, "TasksDB"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val rowView = inflater.inflate(R.layout.inner_list_item, parent, false)
        val textView = rowView.findViewById<TextView>(R.id.innerListElement)

        /*display subtask*/
        textView.text = tasks[position]
        if(isChecked != null && isChecked.contains(position)){
            val checkBox: CheckBox = rowView.findViewById(R.id.innerItemChechkbox)
            checkBox.isChecked = true
            textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            val subTask = textView.text.toString()
            val subTaskFull = db.subTaskDao().selectItem(subTask)
            db.subTaskDao().updateTask(SubTask(id = subTaskFull.id, id_original = subTaskFull.id_original, task = subTaskFull.task, reminder_time = subTaskFull.reminder_time, isDone = true))
        }

        /*update info about subtask being (un)tagged*/
        val checkBox: CheckBox = rowView.findViewById(R.id.innerItemChechkbox)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val textView: TextView = rowView.findViewById(R.id.innerListElement)
            if(isChecked == true){
                textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                val textView1: TextView = rowView.findViewById(R.id.innerListElement)
                val text = textView1.text.toString()
                val subTask: SubTask = db.subTaskDao().selectItem(text)
                db.subTaskDao().updateTask(SubTask(id = subTask.id, task = subTask.task, reminder_time = subTask.reminder_time, isDone = true, id_original = subTask.id_original))
            }

            else textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        return rowView
    }

    /*standart functions overriding*/
    override fun getItem(position: Int): Any {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return tasks.size
    }
}