package com.tonynowater.recyclerview_selection_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_test.view.tv_item

class MyListAdapter : RecyclerView.Adapter<MyListAdapter.MyViewHolder>() {

    var data = mutableListOf("1", "2", "3")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tv_item.text = data[position]
    }

    fun add(random: String) {
        val insertPos = data.size
        data.add(insertPos, random)
        notifyItemInserted(insertPos)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}