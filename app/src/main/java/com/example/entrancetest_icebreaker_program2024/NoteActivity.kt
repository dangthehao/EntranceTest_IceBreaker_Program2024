package com.example.entrancetest_icebreaker_program2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.entrancetest_icebreaker_program2024.adapter.NoteAdapter
import com.example.entrancetest_icebreaker_program2024.helper.NoteDbHelper
import com.example.entrancetest_icebreaker_program2024.model.Note

class NoteActivity : AppCompatActivity() {

    private lateinit var noteListView: ListView
    private lateinit var dbHelper: NoteDbHelper
    private lateinit var notes: ArrayList<Note>
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)

        noteListView = findViewById(R.id.lv_note)
        dbHelper = NoteDbHelper(this)
        notes = dbHelper.getAllNotes()

        adapter = NoteAdapter(this, notes)
        noteListView.adapter = adapter


        findViewById<Button>(R.id.btn_addnote).setOnClickListener {
            startActivity(Intent(this, EditNoteActivity::class.java))
        }

        // Xem chi tiết ghi chú khi nhấn vào một mục trong ListView
        noteListView.setOnItemClickListener { _, _, position, _ ->
            val selectedNote = notes[position]
            val intent = Intent(this, EditNoteActivity::class.java).apply {
                putExtra("NOTE_ID", selectedNote.id) // Truyền id của ghi chú tới activity chỉnh sửa
            }
            startActivity(intent)
        }

        // Xóa ghi chú khi nhấn giữ một mục trong ListView
        noteListView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedNote = notes[position]
            dbHelper.deleteNoteById(selectedNote.id)
            notes.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"Đã Xóa Thành Công",Toast.LENGTH_LONG).show()
            true // Trả về true để ngăn việc xử lý sự kiện khác của ListView
        }
    }

    override fun onResume() {
        super.onResume()
        // Cập nhật danh sách ghi chú khi quay lại từ màn hình chỉnh sửa
        notes.clear()
        notes.addAll(dbHelper.getAllNotes())
        adapter.notifyDataSetChanged()
    }
}
