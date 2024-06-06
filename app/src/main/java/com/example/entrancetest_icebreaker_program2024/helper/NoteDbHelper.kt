package com.example.entrancetest_icebreaker_program2024.helper

import android.content.ContentValues
import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.entrancetest_icebreaker_program2024.TaskContract
import com.example.entrancetest_icebreaker_program2024.model.Note

class NoteDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NotesDatabase"
        private const val TABLE_NOTES = "notes"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NOTES ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_CONTENT TEXT)"
        db.execSQL(createTable)
        if (!hasData(db)) {
            insertDefaultNotes(db)
        }
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun addNote(note: Note): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, note.title)
        values.put(KEY_CONTENT, note.content)
        return db.insert(TABLE_NOTES, null, values)
    }

    fun getAllNotes(): ArrayList<Note> {
        val notesList = ArrayList<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(KEY_ID)
            val titleIndex = it.getColumnIndexOrThrow(KEY_TITLE)
            val contentIndex = it.getColumnIndexOrThrow(KEY_CONTENT)
            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val title = it.getString(titleIndex)
                val content = it.getString(contentIndex)
                val note = Note(id, title, content)
                notesList.add(note)
            }
        }
        return notesList
    }

    fun getNoteById(id: Int): Note {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(KEY_ID, KEY_TITLE, KEY_CONTENT),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it != null && it.moveToFirst()) {
                val titleIndex = it.getColumnIndex(KEY_TITLE)
                val contentIndex = it.getColumnIndex(KEY_CONTENT)
                val title = it.getString(titleIndex)
                val content = it.getString(contentIndex)
                return Note(id, title, content)
            }
        }
        return Note(0, "", "")
    }

    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, note.title)
        values.put(KEY_CONTENT, note.content)
        return db.update(TABLE_NOTES, values, "$KEY_ID=?", arrayOf(note.id.toString()))
    }

    fun deleteNoteById(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NOTES, "$KEY_ID=?", arrayOf(id.toString()))
    }
    private fun hasData(db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NOTES", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }

    private fun insertDefaultNotes(db: SQLiteDatabase) {
        val defaultNotes = arrayOf(
            Note(0, "2/9", "Lễ Quốc Khánh Việt Nam."),
            Note(0, "30/4-1/5", "Giai Phóng Miền Nam Thống Nhất Đất Nước - Quốc Tế Lao Động."),
            Note(0, "1/6", "Ngày Tết Thiếu Nhi."),
            Note(0, "Tăng Cường Thể Lực", "Thức dậy lúc 5h bắt đầu chạy bộ 5km, 20h pushup 100 steps"),
            Note(0, "Học Thêm Ngôn Ngữ lập trình", "Mục Tiêu đặt ra mỗi ngành dành 60p học ngôn ngữ Flutter")

        )

        for (note in defaultNotes) {
            val values = ContentValues().apply {
                put(KEY_TITLE, note.title)
                put(KEY_CONTENT, note.content)
            }
            db.insert(TABLE_NOTES, null, values)
        }
    }
}

