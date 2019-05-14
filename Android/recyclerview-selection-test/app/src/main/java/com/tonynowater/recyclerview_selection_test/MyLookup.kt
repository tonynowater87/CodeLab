package com.tonynowater.recyclerview_selection_test

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MyLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        return rv.findChildViewUnder(e.x, e.y)?.let { view ->
            (rv.getChildViewHolder(view) as MyListAdapter.MyViewHolder).getItemDetails()
        }
    }
}