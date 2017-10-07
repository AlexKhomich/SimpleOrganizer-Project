package com.cdg.alex.simpleorganizer.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdg.alex.simpleorganizer.R
import com.cdg.alex.simpleorganizer.notes_list.NoteContainer
import com.cdg.alex.simpleorganizer.notes_list.NotePriority
import com.cdg.alex.simpleorganizer.notes_list.NotesAdapter
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by alex on 26/10/16.
 */

class NoteFragment : Fragment() {

    var mNotesList: ArrayList<NoteContainer> = ArrayList()
    var mRecyclerNotes: RecyclerView? = null
    var mNotesAdapter: NotesAdapter? = null

    companion object {
        fun newInstance(num: Int): NoteFragment {
            val noteFragment = NoteFragment()
            val args = Bundle()
            noteFragment.arguments = args
            return noteFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater?.inflate(R.layout.fragment_note, container, false)
        val fab = v?.find<FloatingActionButton>(R.id.fabAddNotes)

        mRecyclerNotes = v?.find(R.id.noteRecyclerView)
        mNotesAdapter = NotesAdapter(context, mNotesList)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerNotes?.layoutManager = layoutManager
        mRecyclerNotes?.itemAnimator = DefaultItemAnimator()
        mRecyclerNotes?.adapter = mNotesAdapter

        fab?.setOnClickListener { view ->
            val currentTime = System.currentTimeMillis()
            val sdf: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy  HH:mm", Locale.getDefault())
            val dateAndTime: String = sdf.format(currentTime)
            mNotesList.add(NoteContainer("Test note", dateAndTime, resources.getString(R.string.test_card), NotePriority.LOW))
            mNotesAdapter?.notifyDataSetChanged()
            Snackbar.make(view, "New Note Added", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }

        (mNotesAdapter as NotesAdapter)
        // Inflate the layout for this fragment
        return v
    }
}
