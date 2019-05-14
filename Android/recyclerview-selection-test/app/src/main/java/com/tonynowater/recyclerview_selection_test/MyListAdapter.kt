package com.tonynowater.recyclerview_selection_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_test.view.tv_item

class MyListAdapter : RecyclerView.Adapter<MyListAdapter.MyViewHolder>() {

    var data = mutableListOf("1", "2", "3")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mTracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.isActivated = mTracker?.isSelected(position.toLong())?:false
        holder.itemView.tv_item.text = data[position]
    }

    fun add(random: String) {
        val insertPos = data.size
        data.add(insertPos, random)
        notifyItemInserted(insertPos)
    }

    fun removeAt(position: Int) {
        mTracker?.deselect(position.toLong())
        data.removeAt(position)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getSelectionKey(): Long = itemId
            override fun getPosition(): Int = adapterPosition
        }
    }
}