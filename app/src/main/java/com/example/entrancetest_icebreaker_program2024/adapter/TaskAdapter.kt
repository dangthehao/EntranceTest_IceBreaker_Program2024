package com.example.entrancetest_icebreaker_program2024.adapter


import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.entrancetest_icebreaker_program2024.R
import com.example.entrancetest_icebreaker_program2024.TaskContract
import com.example.entrancetest_icebreaker_program2024.helper.TaskDbHelper
import com.example.entrancetest_icebreaker_program2024.model.Task


class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val dbHelper: TaskDbHelper,
    private val onTaskUpdated: () -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.task_name)
        private val taskDescriptionTextView: TextView = itemView.findViewById(R.id.task_description)
        private val editImageView: ImageView = itemView.findViewById(R.id.edit_task)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.delete_task)

        fun bind(task: Task) {
            taskNameTextView.text = task.name
            taskDescriptionTextView.text = task.description

            editImageView.setOnClickListener {
                showEditTaskDialog(task)
            }

            deleteImageView.setOnClickListener {
                showDeleteTaskDialog(task)
            }
        }

        private fun showEditTaskDialog(task: Task) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Chỉnh sửa nhiệm vụ")

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
            val taskNameEditText: TextView = view.findViewById(R.id.edit_task_name)
            val taskDescriptionEditText: TextView = view.findViewById(R.id.edit_task_description)

            taskNameEditText.text = task.name
            taskDescriptionEditText.text = task.description

            builder.setView(view)
            builder.setPositiveButton("Lưu") { dialog, _ ->
                val newName = taskNameEditText.text.toString()
                val newDescription = taskDescriptionEditText.text.toString()
                if (newName.isNotEmpty() && newDescription.isNotEmpty()) {
                    task.name = newName
                    task.description = newDescription
                    updateTask(task)
                    onTaskUpdated()
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("Hủy") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }

        private fun showDeleteTaskDialog(task: Task) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Xóa nhiệm vụ")
            builder.setMessage("Bạn có chắc chắn muốn xóa nhiệm vụ này?")
            builder.setPositiveButton("Xóa") { dialog, _ ->
                deleteTask(task)
                onTaskUpdated()
                dialog.dismiss()
            }
            builder.setNegativeButton("Hủy") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        private fun updateTask(task: Task) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task.name)
                put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION, task.description)
            }
            db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(task.id.toString())
            )
        }

        private fun deleteTask(task: Task) {
            val db = dbHelper.writableDatabase
            db.delete(
                TaskContract.TaskEntry.TABLE_NAME,
                "${BaseColumns._ID} = ?",
                arrayOf(task.id.toString())
            )
            tasks.remove(task)
            notifyDataSetChanged()
        }
    }
}
