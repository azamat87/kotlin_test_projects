package kz.mars.studio.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

/**
 * Created by Asus on 04.02.2018.
 */
class DbManager {
    val bdName = "MyNotes"
    val dbTable = "Notes"

    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
    //CREATE TABLE IF NOT EXISTS MyNotes (ID INTEGER PRIMARY KEY,Title TEXT, Description TEXT);
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colID + " INTEGER PRIMARY KEY," +
            colTitle + " TEXT, " + colDes + " TEXT);"

    var sqlDB: SQLiteDatabase? = null


    constructor(context: Context) {
        var db = DataBaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    fun insert(value: ContentValues): Long{
        val ID = sqlDB!!.insert(dbTable, "", value)
        return ID
    }

    fun query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {

        val qd = SQLiteQueryBuilder()
        qd.tables = dbTable
        val cursor = qd.query(sqlDB,projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }

    fun update(value: ContentValues, selection: String, selectionArgs: Array<String>): Int {

        val count = sqlDB!!.update(dbTable, value, selection, selectionArgs)
        return count
    }

    inner class DataBaseHelperNotes: SQLiteOpenHelper{
        var ctx: Context? = null

        constructor(context: Context): super(context, bdName, null, dbVersion){
            this.ctx = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(ctx, " DB onCreate", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXIST " + dbTable)
        }
    }
}