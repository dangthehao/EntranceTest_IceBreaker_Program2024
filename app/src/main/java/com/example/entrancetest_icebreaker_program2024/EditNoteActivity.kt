package com.example.entrancetest_icebreaker_program2024

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.entrancetest_icebreaker_program2024.helper.NoteDbHelper
import com.example.entrancetest_icebreaker_program2024.model.Note

class EditNoteActivity : AppCompatActivity() {

    private lateinit var noteTitleEditText: EditText
    private lateinit var noteContentEditText: EditText
    private lateinit var saveNoteButton: Button
    private lateinit var dbHelper: NoteDbHelper
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        noteTitleEditText = findViewById(R.id.edt_title)
        noteContentEditText = findViewById(R.id.edt_content)
        saveNoteButton = findViewById(R.id.btn_save)
        dbHelper = NoteDbHelper(this)

        // Lấy id của ghi chú được chọn từ Intent
        noteId = intent.getIntExtra("NOTE_ID", 0)

        if (noteId != 0) {
            // Nếu có id, hiển thị thông tin của ghi chú đó để chỉnh sửa
            val note = dbHelper.getNoteById(noteId)
            noteTitleEditText.setText(note.title)
            noteContentEditText.setText(note.content)
        }

        saveNoteButton.setOnClickListener {
            val title = noteTitleEditText.text.toString()
            val content = noteContentEditText.text.toString()

            // Kiểm tra xem tiêu đề và nội dung của ghi chú có rỗng hay không
            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (noteId != 0) {
                    // Nếu có id, cập nhật ghi chú hiện có
                    val updatedNote = Note(noteId, title, content)
                    dbHelper.updateNote(updatedNote)
                } else {
                    // Nếu không có id, thêm ghi chú mới
                    val newNote = Note(0, title, content)
                    dbHelper.addNote(newNote)
                }
                finish() // Kết thúc activity và quay về màn hình chính
            } else {
                // Hiển thị thông báo lỗi nếu tiêu đề hoặc nội dung trống
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
