package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.notesapp.room.Constant
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Types.NULL

class EditActivity : AppCompatActivity() {

    private val db by lazy { NoteDB(this) }
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
    }

    private fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        when (intentType()) {  // Do action according to intentType
            Constant.TYPE_CREATE -> {
                supportActionBar!!.title = "New Note"
                createButton.visibility = View.VISIBLE
                updateButton.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                supportActionBar!!.title = "My Note"
                createButton.visibility = View.GONE
                updateButton.visibility = View.GONE
                titleEditText.isEnabled = false
                noteEditText.isEnabled = false
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                supportActionBar!!.title = "Edit Note"
                createButton.visibility = View.GONE
                updateButton.visibility = View.VISIBLE
                getNote()
            }
        }
    }

    private fun setupListener(){
        createButton.setOnClickListener {
            if(TextUtils.isEmpty(titleEditText.text)) { // Check if EditText is empty
                Toast.makeText(this, "Please fill the title field!", Toast.LENGTH_SHORT).show()  // Make Toast
                titleEditText.requestFocus()  // Set focus to Edit Text
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    // Create new note
                    db.noteDao().createNote(Note(0, titleEditText.text.toString(), noteEditText.text.toString()))
                    finish()  // Close current activity
                }
            }
        }
        updateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // Update the note
                db.noteDao().updateNote(Note(noteId, titleEditText.text.toString(), noteEditText.text.toString()))
                finish()  // Close current activity
            }
        }
    }

    // Get specific note
    private fun getNote(){
        noteId = intent.getIntExtra("note_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().readNote(noteId)[0]  // Read note with specific id
            titleEditText.setText( notes.title )
            noteEditText.setText( notes.note )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()  // Close current activity
        return super.onSupportNavigateUp()
    }

    // Get intentType to decide validated action
    private fun intentType(): Int {
        return intent.getIntExtra("intent_type", 0)
    }
}