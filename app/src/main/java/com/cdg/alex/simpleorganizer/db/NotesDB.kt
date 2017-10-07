package com.cdg.alex.simpleorganizer.db

import io.realm.RealmObject

open class NotesDB(var name: String? = "",
                   var priority: String? = "LOW",
                   var info: String? = "",
                   var dateAndTime: String? = "",
                   var attachmentPath: String? = "") : RealmObject() {

}