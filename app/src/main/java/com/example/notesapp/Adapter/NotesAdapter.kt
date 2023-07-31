package com.example.notesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.Models.Note
import com.example.notesapp.R

class NotesAdapter(private val context: Context, val listener: MainActivity) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentNote = NotesList[position]
        holder.tvTitle.text = currentNote.title
        holder.tvTitle.isSelected = true
        holder.tvNote.text = currentNote.note
        holder.tvDate.text = currentNote.date
        holder.tvDate.isSelected = true


        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemclick(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }

    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search : String){

        NotesList.clear()

        for(item in fullList){

            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){

                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvNote = itemView.findViewById<TextView>(R.id.tv_note)
        val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface NoteClickListener {

        fun onItemClicked(note: Note)
        fun onLongItemclick(note:Note, cardView: CardView)
    }

}