package com.example.contentprovider

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

// this program can add nicknames to a database
class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    // creates the database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // handles action bar item clicks
     fun OnOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // handles navigation item view clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // adds records to the database
    public fun addRecord(view: View) {
        var values = ContentValues()
        if (!(name.text.toString().isEmpty())
            && (!(nickname.text.toString().isEmpty()))) {
            values.put(CustomContentProvider.NAME, name.text.toString())
            values.put(CustomContentProvider.NICK_NAME, nickname.text.toString())
            contentResolver.insert(CustomContentProvider.CONTENT_URI, values)
            Toast.makeText(baseContext, "Record Inserted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(baseContext, "Please enter the records first", Toast.LENGTH_LONG).show()
        }
    }

    // shows all records in the database
    public fun showAllRecords(view: View) {
        // show all records sorted by friend's name
        val URL = "content://com.androidatc.provider/nicknames"
        val friends = Uri.parse(URL)
        val c = contentResolver.query(friends, null, null, null, "name")
        var result = "Content Provider Results:"
            if (c!!.moveToFirst()) {
                Toast.makeText(this, result + " no content yet!", Toast.LENGTH_LONG).show()
            } else {
                do {
                    result += "\n${c.getString(c.getColumnIndex(CustomContentProvider.NAME))} " +
                            "with id ${c.getString(c.getColumnIndex(CustomContentProvider.ID))} " +
                            "has nickname:${c.getString(c.getColumnIndex(CustomContentProvider.NICK_NAME))}"

                } while (c.moveToNext())
                if (result.isEmpty())
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this, "No Records present", Toast.LENGTH_LONG).show()
            }
        }

        // delete all the records in the database
         public fun deleteAllRecords(view: View) {
            val URL = "content://com.androidatc.provider/nicknames"
            var friends = Uri.parse(URL)
            var count = contentResolver.delete(friends, null, null)
            var countNum = "$count records are deleted."
            Toast.makeText(baseContext, countNum, Toast.LENGTH_LONG).show()

        }



}