package com.example.room.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.room.R
import com.example.room.database.Poem
import com.example.room.database.PoemDao
import com.example.room.database.PoemRoomDatabase
import com.example.room.databinding.ListItemBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TabAdapter (var listPoem: List<Poem>?): RecyclerView.Adapter<TabAdapter.MyViewHolder>(){
    lateinit var executorService: ExecutorService
    lateinit var mNotesDao: PoemDao

    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val tittle = view.findViewById<TextView>(R.id.title_detail)
        val poem = view.findViewById<TextView>(R.id.poem_detail)
        val btnUpdate = view.findViewById<Button>(R.id.update_btn)
        val btnDelete = view.findViewById<Button>(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(listPoem!=null){
            return listPoem!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        executorService = Executors.newSingleThreadExecutor()
        val db = PoemRoomDatabase.getDatabase(holder.itemView.context)
        mNotesDao = db!!.PoemDao()!!

        holder.tittle.text = "tittle : ${listPoem?.get(position)?.title}"
        holder.poem.text = "description : ${listPoem?.get(position)?.poem}"

        holder.btnUpdate.setOnClickListener {
            val intentToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentToDetail.putExtra("ID", listPoem?.get(position)?.id)
            intentToDetail.putExtra("TITTLE", listPoem?.get(position)?.title)
            intentToDetail.putExtra("POEM", listPoem?.get(position)?.poem)
            intentToDetail.putExtra("COMMAND", "UPDATE")
            holder.itemView.context.startActivity(intentToDetail)
        }

        holder.btnDelete.setOnClickListener {
            val noteId = listPoem?.get(position)?.id
            noteId?.let { deleteNoteById(it) }
            Toast.makeText(holder.itemView.context, "Berhasil Menghapus Data", Toast.LENGTH_SHORT)
                .show()
            true
        }

    }
    private fun deleteNoteById(poemId: Int) {
        executorService.execute {
            mNotesDao.deleteById(poemId)
        }
    }



//    inner class ItemPoemViewHolder(private val binding: ListItemBinding):
//        RecyclerView.ViewHolder(binding.root){

//        init {
//            itemView.setOnLongClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val clickedPoem = listPoem[position]
//                    onClickPoem(clickedPoem)
//                    true
//                } else {
//                    false
//                }
//            }
//        }
//        fun bind(data: Poem){
//            with(binding){
//                titleDetail.text = data.title
//                poemDetail.text = data.poem
//            }
//        }
//    }




//
//    override fun getItemCount(): Int = listPoem.size
//
//    fun setData(newData: List<Poem>) {
//        this.listPoem = newData
//        notifyDataSetChanged()
//    }


}