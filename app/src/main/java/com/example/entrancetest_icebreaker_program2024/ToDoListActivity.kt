package com.example.entrancetest_icebreaker_program2024
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entrancetest_icebreaker_program2024.adapter.TaskAdapter
import com.example.entrancetest_icebreaker_program2024.helper.TaskDbHelper
import com.example.entrancetest_icebreaker_program2024.model.Task

import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoListActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist)

        dbHelper = TaskDbHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab_addtask)

        fab.setOnClickListener {
            showAddTaskDialog()
        }

        setupRecyclerView()
    }



    override fun onResume() {
        super.onResume()
        displayTasks()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(this, tasks, dbHelper) {
            displayTasks() // Callback to refresh the task list
        }
        recyclerView.adapter = adapter
    }

    private fun displayTasks() {
        tasks.clear()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            TaskContract.TaskEntry.COLUMN_TASK_NAME,
            TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION
        )
        val cursor: Cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val name = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK_NAME))
                val description = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION))
                tasks.add(Task(id, name, description))
            }
        }
        adapter.notifyDataSetChanged()
        cursor.close()
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thêm nhiệm vụ")

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_task, null)
        val taskNameEditText: TextView = view.findViewById(R.id.edit_task_name)
        val taskDescriptionEditText: TextView = view.findViewById(R.id.edit_task_description)

        builder.setView(view)
        builder.setPositiveButton("Thêm") { dialog, _ ->
            val name = taskNameEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            if (name.isNotEmpty() && description.isNotEmpty()) {
                addTask(name, description)
                displayTasks()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addTask(name: String, description: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_TASK_NAME, name)
            put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION, description)
        }
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
    }
}
