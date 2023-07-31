package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.Models.Note

@Dao
interface NoteDao {

    //@Insert() =
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes() : LiveData<List<Note>>

    //When update() is called we get the id, title, note that has to be update. We then update the notes_table with the id, title and note
    @Query("UPDATE notes_table Set title = :title , note = :note WHERE id = :id")
    suspend fun update(id: Int?, title : String?, note: String? )

}