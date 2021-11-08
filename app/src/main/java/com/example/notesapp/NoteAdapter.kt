package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.room.Note
import kotlinx.android.synthetic.main.notes.view.*

class NoteAdapter(private val notes: ArrayList<Note>, private val listener: OnAdapterListener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.notes, parent, false)
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.itemView.titleTextView.text = note.title
        holder.itemView.titleTextView.setOnClickListener {
            listener.onNew(note)
        }
        holder.itemView.editImageView.setOnClickListener {
            listener.onUpdate(note)
        }
        holder.itemView.deleteImageView.setOnClickListener {
            listener.onDelete(note)
        }
    }

    fun setData(newList: List<Note>) {
        notes.clear()
        notes.addAll(newList)
    }

    interface OnAdapterListener {
        fun onNew(note: Note)
        fun onUpdate(note: Note)
        fun onDelete(note: Note)
    }
}