package com.cdg.alex.simpleorganizer.activities

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.cdg.alex.simpleorganizer.Notes.Note
import com.cdg.alex.simpleorganizer.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class NoteActivity : AppCompatActivity() {

    private var isFabMenuOpen = false
    private var fab1: FloatingActionButton? = null
    private var fab2: FloatingActionButton? = null
    private var tvData: TextView by Delegates.notNull()
    private var realm: Realm by Delegates.notNull()
    private var actionBar: ActionBar by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        actionBar = this.supportActionBar!!
        realm = Realm.getDefaultInstance()
        val fab: FloatingActionButton = fabNotesOptions
        fab1 = fabNotes1
        fab2 = fabNotes2
        tvData = et_note_data

        fab.setOnClickListener { _ ->
            if (!isFabMenuOpen) {
                showFabMenu()
            } else {
                hideFabMenu()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notes_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.save_note -> saveNoteToDB("Test", "Low", tvData.text.toString(), getDateAndTime(), "")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNoteToDB(name: String, priority: String, data: String, date: String, attachmentPath: String) {
        realm.executeTransaction { realm ->
            val note = Note(name, priority, data, date, attachmentPath)
            realm.copyToRealmOrUpdate(note)
            realm.commitTransaction()
        }
    }

    private fun getDateAndTime(): String {
        val currentTime = System.currentTimeMillis()
        val sdf: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy  HH:mm", Locale.getDefault())
        return sdf.format(currentTime)
    }

    private fun showFabMenu() {
        isFabMenuOpen = true
        fab1?.visibility = View.VISIBLE
        fab2?.visibility = View.VISIBLE
    }

    private fun hideFabMenu() {
        isFabMenuOpen = false
        fab1?.visibility = View.GONE
        fab2?.visibility = View.GONE
    }
}
