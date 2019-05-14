package com.tonynowater.recyclerview_selection_test

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class MyItemKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {

    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {

        /*//solution 1 not ok
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION*/

        /*//solution 2 display ok, delete ok
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition?:key.toInt()*/

        //solution 3 display ok, delete ok
        return key.toInt()
    }
}