package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import com.example.notesapp.Models.Note
import com.example.notesapp.databinding.ActivityEditNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class EditNote : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var note : Note
    private lateinit var old_note : Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewBack
        binding.imageViewSave

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitleEdit.setText(old_note.title)
            binding.etNoteEdit.setText(old_note.note)
            isUpdate = true


        } catch (e: Exception){

            e.printStackTrace()

        }

        binding.imageViewSave.setOnClickListener {

            val title = binding.etTitleEdit.text.toString()
            val note_desc = binding.etNoteEdit.text.toString()

            if (title.isEmpty() || note_desc.isNotEmpty()){

                val formatter = SimpleDateFormat.getDateInstance()

                if(isUpdate){

                    note = Note(
                        old_note.id,title,note_desc,formatter.format(Date())
                    )
                } else {

                    note = Note(
                        null, title, note_desc,formatter.format(Date())
                    )
                }
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            } else {

                Toast.makeText(this@EditNote,"Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

        }
        binding.imageViewBack.setOnClickListener {


            val intent = Intent(this@EditNote, MainActivity::class.java)
            startActivity(intent)
        }

    }
}