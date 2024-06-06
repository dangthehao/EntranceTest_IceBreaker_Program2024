package com.example.entrancetest_icebreaker_program2024.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.entrancetest_icebreaker_program2024.model.Note

class NoteAdapter(context: Context, notes: ArrayList<Note>) :
    ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val note = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        val titleTextView = convertView?.findViewById<TextView>(android.R.id.text1)
        titleTextView?.text = note?.title
        return convertView!!
    }
}

