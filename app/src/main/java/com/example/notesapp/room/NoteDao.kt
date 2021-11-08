package com.example.notesapp.room

import androidx.room.*

@Dao
interface NoteDao { // Dao = Data Access Object

    @Insert
    suspend fun createNote(note: Note)

    @Query("SELECT * FROM note")
    suspend fun readNotes() : List<Note>

    @Query("SELECT * FROM note WHERE id=:note_id")
    suspend fun readNote(note_id: Int) : List<Note>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}