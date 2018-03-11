package com.cdg.alex.simpleorganizer.Notes

import io.realm.RealmObject

open class Note(var name: String = "",
                var priority: String = "Low",
                var info: String = "",
                var dateAndTime: String = "",
                var attachmentPath: String = "") : RealmObject()

