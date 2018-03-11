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
import com.cdg.alex.simpleorganizer.Notes.Note
import com.cdg.alex.simpleorganizer.Notes.notes_list.NoteContainer
import com.cdg.alex.simpleorganizer.Notes.notes_list.NotePriority
import com.cdg.alex.simpleorganizer.Notes.notes_list.NotesAdapter
import com.cdg.alex.simpleorganizer.R
import io.realm.Realm
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * Created by alex on 26/10/16.
 */

class NoteFragment : Fragment() {

    var mNotesList: ArrayList<NoteContainer> = ArrayList()
    var mRecyclerNotes: RecyclerView? = null
    var mNotesAdapter: NotesAdapter? = null
    private var realm: Realm by Delegates.notNull()
    private val NOTE_PRIORITY_HEIHGT = "Height"
    private val NOTE_PRIORITY_MEDIUM = "Medium"
    private val NOTE_PRIORITY_LOW = "Low"

    companion object {
        fun newInstance(num: Int): NoteFragment {
            val noteFragment = NoteFragment()
            val args = Bundle()
            noteFragment.arguments = args
            return noteFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(context)
        realm = Realm.getDefaultInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realm.close()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater?.inflate(R.layout.fragment_note, container, false)
        val fab = v?.find<FloatingActionButton>(R.id.fabAddNotes)

        mRecyclerNotes = v?.find(R.id.noteRecyclerView)
        mNotesAdapter = NotesAdapter(context, getNotesList())

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerNotes?.layoutManager = layoutManager
        mRecyclerNotes?.itemAnimator = DefaultItemAnimator()
        mRecyclerNotes?.adapter = mNotesAdapter

        fab?.setOnClickListener { view ->
            val currentTime = System.currentTimeMillis()
            val sdf: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy  HH:mm", Locale.getDefault())
            val dateAndTime: String = sdf.format(currentTime)
            mNotesList.add(NoteContainer("Test note", dateAndTime, resources.getString(R.string.test_card), NotePriority.LOW, ""))
            mNotesAdapter?.notifyDataSetChanged()
            Snackbar.make(view, "New Note Added", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }

        (mNotesAdapter as NotesAdapter)
        // Inflate the layout for this fragment
        return v
    }

    private fun getNotesList(): ArrayList<NoteContainer> {
        var notePriority = NotePriority.LOW
        if (!realm.isClosed && !realm.isEmpty) {
            for (note in realm.where(Note::class.java).findAll()) {
                when (note.priority) {
                    NOTE_PRIORITY_HEIHGT -> notePriority = NotePriority.HEIGHT
                    NOTE_PRIORITY_MEDIUM -> notePriority = NotePriority.MEDIUM
                    NOTE_PRIORITY_LOW -> notePriority = NotePriority.LOW
                }
                mNotesList.add(NoteContainer(note.name, note.dateAndTime, note.info, notePriority, note.attachmentPath))
            }
        }

        return mNotesList
    }
}
