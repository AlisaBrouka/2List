package by.bsuir.a2list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.room.Room
import by.bsuir.a2list.Database.Database

/*we pass list of subtasks to create this adapter
this adapter helps to display subtasks when we add a new task*/

class AddTaskAdapter(private val context: Context, private val listElements: ArrayList<String>?): BaseAdapter() {
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /*open db*/
    val db = Room.databaseBuilder(
        context,
        Database::class.java, "TasksDB"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.add_task_element, parent, false)
        val editText: EditText = rowView.findViewById(R.id.addSubTask)

        //display adding subtask
        editText.setText(listElements!![position])
        return rowView
    }

    /*standart function override*/
    override fun getItem(position: Int): Any {
        return listElements!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {

        return when{
            listElements!= null -> listElements.size
            else -> 0
        }
    }
}