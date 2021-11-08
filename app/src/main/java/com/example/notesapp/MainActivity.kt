package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.room.Constant
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private val db by lazy { NoteDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListener()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            noteAdapter.setData(db.noteDao().readNotes())
            withContext(Dispatchers.Main) {
                noteAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupView (){
        supportActionBar!!.apply {
            title = "All Notes"
        }
    }

    private fun setupListener(){
        newButton.setOnClickListener {
            intentEdit(Constant.TYPE_CREATE, 0)
        }
    }

    private fun setupRecyclerView () {
        noteAdapter = NoteAdapter(
                arrayListOf(),
                object : NoteAdapter.OnAdapterListener {
                    override fun onNew(note: Note) {
                        intentEdit(Constant.TYPE_READ, note.id)
                    }

                    override fun onUpdate(note: Note) {
                        intentEdit(Constant.TYPE_UPDATE, note.id)
                    }

                    override fun onDelete(note: Note) {
                        deleteAlert(note)
                    }
                })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    private fun intentEdit(intent_type: Int, note_id: Int) {
        startActivity(
                Intent(this, EditActivity::class.java)
                        .putExtra("intent_type", intent_type)
                        .putExtra("note_id", note_id)
        )
    }

    private fun deleteAlert(note: Note){
        val dialog = AlertDialog.Builder(this, R.style.AlertDialog)
        dialog.apply {
            setTitle("Delete note")
            setMessage("Do you want to delete ${note.title}?")
            setNegativeButton("No") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Yes") { dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    dialogInterface.dismiss()
                    loadData()
                }
            }
        }.show()
    }
}