package com.example.entrancetest_icebreaker_program2024.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.entrancetest_icebreaker_program2024.TaskContract

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_TASKS_TABLE = "CREATE TABLE " +
                TaskContract.TaskEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION + " TEXT);"
        db.execSQL(SQL_CREATE_TASKS_TABLE)
        if (!hasData(db)) {
            insertDefaultTasks(db)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME)
        onCreate(db)
    }
    private fun hasData(db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM " + TaskContract.TaskEntry.TABLE_NAME, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }

    private fun insertDefaultTasks(db: SQLiteDatabase) {
        val defaultTasks = arrayOf(
            Pair("goToTheMoon", "Khám Phá Ngân Hà"),
            Pair("goBackHome", "Về Nhà trước 00h"),
            Pair("speedRun", "Chạy bộ 5km trong 20p")
        )

        for (task in defaultTasks) {
            val values = ContentValues().apply {
                put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task.first)
                put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION, task.second)
            }
            db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
        }
    }




    companion object {
        private const val DATABASE_NAME = "tasksDb.db"
        private const val DATABASE_VERSION = 1
    }
}
