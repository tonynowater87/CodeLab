package com.tonynowater.recyclerview_selection_test

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerSpaceDecoration(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, space)
    }
}