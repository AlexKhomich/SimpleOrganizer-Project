package com.cdg.alex.simpleorganizer.activities

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cdg.alex.simpleorganizer.R
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    var isFabMenuOpen = false
    var fab1: FloatingActionButton? = null
    var fab2: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val fab: FloatingActionButton = fabNotesOptions
        fab1 = fabNotes1
        fab2 = fabNotes2

        fab.setOnClickListener { _ ->
            if (!isFabMenuOpen) {
                showFabMenu()
            } else {
                hideFabMenu()
            }
        }
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
