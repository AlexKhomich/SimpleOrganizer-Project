package com.cdg.alex.simpleorganizer.notes_list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdg.alex.simpleorganizer.R
import kotlinx.android.synthetic.main.card_note.view.*
import java.util.*

/**
 * Created by alex on 03/06/17.
 */
class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NoteContainerHolder>() {

    var context: Context? = null
    var mNotesHolderList: ArrayList<NoteContainer>? = null
    var mNoteData: NoteContainer? = null

    constructor(context: Context, notesList: ArrayList<NoteContainer>) : this() {
        this.context = context
        this.mNotesHolderList = notesList
    }

    inner class NoteContainerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView?.textViewName
        val tvDateAndTime = itemView?.textViewDateAndTime
        val tvOptionDigit = itemView?.txtOptionDigit
        val tvNotePrev = itemView?.textViewNotePrev
        val imPriority = itemView?.imageViewPriority
    }

    override fun onBindViewHolder(holder: NoteContainerHolder?, position: Int) {
        mNoteData = mNotesHolderList?.get(holder!!.adapterPosition)
        holder?.tvName?.text = mNoteData!!.noteName
        holder?.tvDateAndTime?.text = mNoteData!!.noteDateAndTime
        holder?.tvNotePrev?.text = mNoteData!!.noteData
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NoteContainerHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_note, parent, false)
        return NoteContainerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mNotesHolderList!!.size
    }


}