package com.example.room.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.room.database.Poem
import com.example.room.databinding.ListItemBinding


class TabAdapter (private var listPoem: List<Poem>, var
onClickPoem: (Poem)-> Unit): RecyclerView.Adapter<TabAdapter.ItemPoemViewHolder>(){

    inner class ItemPoemViewHolder(private val binding: ListItemBinding):
        RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedPoem = listPoem[position]
                    onClickPoem(clickedPoem)
                    true
                } else {
                    false
                }
            }
        }
        fun bind(data: Poem){
            with(binding){
                titleDetail.text = data.title
                poemDetail.text = data.poem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPoemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
        return ItemPoemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemPoemViewHolder, position: Int) {
        holder.bind(listPoem[position])
    }

    override fun getItemCount(): Int = listPoem.size

    fun setData(newData: List<Poem>) {
        this.listPoem = newData
        notifyDataSetChanged()
    }


}