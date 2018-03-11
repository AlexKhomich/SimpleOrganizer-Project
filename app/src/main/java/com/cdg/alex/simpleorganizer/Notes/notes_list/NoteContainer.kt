package com.cdg.alex.simpleorganizer.Notes.notes_list

/**
 * Created by alex on 03/06/17.
 */
data class NoteContainer(var noteName: String, var noteDateAndTime: String, var noteData: String, var notePriority: NotePriority, var attachmentPath: String) {
}