package com.example.notesapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.NotesAdapter
import com.example.notesapp.Database.NoteDatabase
import com.example.notesapp.Models.Note
import com.example.notesapp.Models.NoteViewModel
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NotesAdapter.NoteClickListener {

    private  lateinit var binding : ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null) {

                viewModel.updateNote(note)
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddNote
        binding.recyclerView
        binding.searchView

        initUI()

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel .allNotes.observe(this) { list ->

            list?.let {

                adapter.updateList(list)

            }
        }

        database = NoteDatabase.getDatabase(this)


    }

    private fun initUI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {

                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {

                        viewModel.insertNote(note)
                    }
                }
            }

        binding.buttonAddNote.setOnClickListener {

            val intent = Intent(this, EditNote::class.java)
            getContent.launch(intent)

        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)

                }
                return false
            }
        })

    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, EditNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)


    }

    override fun onLongItemclick(note: Note, cardView: CardView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setPositiveButton("Delete") { _, _ ->
            deleteDatabase("")
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()

        viewModel.deleteNote(note)
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()


        }

    }
