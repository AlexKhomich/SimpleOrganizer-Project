package com.cdg.alex.simpleorganizer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by alex on 26/10/16.
 */
class DBHandler (context: Context) : SQLiteOpenHelper(context, DBHandler.DATABASE_NAME, null, DBHandler.DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {


    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "notesDB"
        private val TABLE_NOTES = "notes"
        private val KEY_ID = "id"
        private val KEY_HEADER = "header"
        private val KEY_DATA = "data"
    }


}