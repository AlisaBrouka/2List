package by.bsuir.a2list

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {

        /*creating page view*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        /*activating bottom navigation menu*/
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        /*recovery previous switch status from memory*/
        val prefs = this.getSharedPreferences("onOfButton", MODE_PRIVATE)
        val booleanFromPreferences: Boolean = prefs.getBoolean("isChecked", true)

        /*set switch custom view, switch status*/
        val onOffButton: Switch = findViewById(R.id.onOffButton)
        onOffButton.isChecked = booleanFromPreferences

        /* every switch status change is being saved in memory*/
        onOffButton.setOnCheckedChangeListener{ _, isChecked ->
            val editor = prefs.edit()
            editor.putBoolean("isChecked", isChecked)
            editor.apply()
        }
    }
}