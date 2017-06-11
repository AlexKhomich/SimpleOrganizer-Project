package com.cdg.alex.simpleorganizer.notes_list

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupMenu
import com.cdg.alex.simpleorganizer.R
import kotlinx.android.synthetic.main.card_note.view.*

/**
 * Created by alex on 03/06/17.
 */
class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NoteContainerHolder>(), AdapterView.OnItemClickListener {

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
        mNoteData = holder?.adapterPosition?.let { mNotesHolderList?.get(it) }
        holder?.tvName?.text = mNoteData?.noteName
        holder?.tvDateAndTime?.text = mNoteData?.noteDateAndTime
        holder?.tvNotePrev?.text = mNoteData?.noteData

        val stringArray = context?.resources?.getStringArray(R.array.priorities)
        holder?.tvOptionDigit?.setOnClickListener { view ->
            val popupMenu: PopupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.menu_notes)
            popupMenu.setOnMenuItemClickListener({ item: MenuItem? ->
                when (item?.itemId) {
                    R.id.delete_note -> {
                        Log.d("Note", "position: ${holder.adapterPosition}")
                        mNotesHolderList?.removeAt(holder.adapterPosition)
                        notifyDataSetChanged()
                        false
                    }
                    R.id.set_priority -> {
                        val alertDialog = AlertDialog.Builder(context)
                                .setTitle("Priorities levels")
                                .setSingleChoiceItems(stringArray, 0, { _, _ -> })
                                .setPositiveButton("Ok", { dialog, _ ->
                                    val listView: ListView = (dialog as AlertDialog).listView
                                    val items = listView.checkedItemPosition
                                    val buffer = stringArray?.get(items)
                                    when (buffer) {
                                        "Low" -> holder.imPriority?.setImageResource(R.drawable.pr_green)
                                        "Medium" -> holder.imPriority?.setImageResource(R.drawable.pr_yellow)
                                        "Height" -> holder.imPriority?.setImageResource(R.drawable.pr_red)
                                    }
                                }).setNegativeButton("Cancel", { _, _ -> })
                        alertDialog.create().show()
                        false
                    }
                    else -> {
                        false
                    }
                }
            })
            popupMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NoteContainerHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_note, parent, false)
        return NoteContainerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mNotesHolderList!!.size
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}