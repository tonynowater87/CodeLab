package com.tonynowater.recyclerview_selection_test

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.reccyclerview

class MainActivity : AppCompatActivity() {

    private var mSelectTracker: SelectionTracker<Long>? = null
    private var mActionMode: ActionMode? = null

    private lateinit var mAdapter: MyListAdapter

    private val mActionModeCallback = object : ActionMode.Callback {
        private var mIsSelectedAll: Boolean = false
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            Log.d(Constant.DEBUG, "onActionItemClicked${item?.itemId}")
            when (item.itemId) {
                R.id.action_delete -> {
                    mSelectTracker?.selection?.sortedDescending()?.forEach {
                        mAdapter.removeAt(it.toInt())
                        mAdapter.notifyDataSetChanged()
                    }
                }
                R.id.action_select_all -> {
                    if (mIsSelectedAll) {
                        mIsSelectedAll = false
                        selectedAll(mIsSelectedAll)
                    } else {
                        mIsSelectedAll = true
                        selectedAll(mIsSelectedAll)
                    }
                }
            }
            return true
        }

        private fun selectedAll(selectedAll: Boolean) {
            mAdapter.data.forEachIndexed { index, _ ->
                val position = index.toLong()
                val positionIsSelected = mSelectTracker?.isSelected(position)
                Log.d(Constant.DEBUG, "action_select_all:$index $positionIsSelected")
                when (selectedAll) {
                    true -> {
                        mSelectTracker?.select(position)
                    }
                    false -> {
                        mSelectTracker?.deselect(position)
                    }
                }
            }
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            Log.d(Constant.DEBUG, "onCreateActionMode")
            mIsSelectedAll = false
            mode.menuInflater.inflate(R.menu.menu_delete_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu?): Boolean {
            Log.d(Constant.DEBUG, "onPrepareActionMode")
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            Log.d(Constant.DEBUG, "onDestroyActionMode")
            mSelectTracker?.clearSelection()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        savedInstanceState?.let {
            mSelectTracker?.onRestoreInstanceState(it)
        }

        mAdapter = MyListAdapter()
        reccyclerview.layoutManager = LinearLayoutManager(this)
        reccyclerview.adapter = mAdapter
        buildSelectionTracker(recyclerView = reccyclerview)

        fab.setOnClickListener {
            val random = mAdapter.itemCount.toString()
            mAdapter.add(random)
            updateActionModeTitle()//add data to update title
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            mSelectTracker?.onSaveInstanceState(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val mCustomSelectionPredicates: SelectionTracker.SelectionPredicate<Long> =
        object : SelectionTracker.SelectionPredicate<Long>() {
            override fun canSelectMultiple(): Boolean {
                return true
            }

            override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
                return true
            }

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                return true
            }
        }

    private fun buildSelectionTracker(recyclerView: RecyclerView) {
        if (mSelectTracker == null) {
            mSelectTracker = SelectionTracker.Builder<Long>(
                "multi-selection-test",
                recyclerView,
                MyItemKeyProvider(recyclerView),
                /*StableIdKeyProvider(recyclerView),*/
                MyLookup(recyclerView),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                mCustomSelectionPredicates//自定義：可決定item是否可以select
                /*SelectionPredicates.createSelectSingleAnything()//單選*/
                /*SelectionPredicates.createSelectAnything()//多選*/
            ).build()
                .apply {
                    this.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                        override fun onItemStateChanged(key: Long, selected: Boolean) {
                            super.onItemStateChanged(key, selected)
                            Log.d(Constant.DEBUG, "onItemStateChanged key:$key, selected:$selected")
                        }

                        override fun onSelectionChanged() {
                            super.onSelectionChanged()
                            Log.d(Constant.DEBUG, "onSelectionChanged")
                            val selectItems: Int? = mSelectTracker?.selection?.size()
                            val isInMultiSelectionMode = selectItems != null && selectItems > 0
                            if (isInMultiSelectionMode) {
                                if (mActionMode == null) {
                                    mActionMode = startSupportActionMode(mActionModeCallback)
                                }
                            } else {
                                mActionMode?.finish()
                                mActionMode = null
                            }
                            updateActionModeTitle()
                        }

                        override fun onSelectionRefresh() {
                            super.onSelectionRefresh()
                            Log.d(Constant.DEBUG, "onSelectionRefresh")
                        }

                        override fun onSelectionRestored() {
                            super.onSelectionRestored()
                            Log.d(Constant.DEBUG, "onSelectionRestored")
                        }
                    })
                }
            mAdapter.mTracker = mSelectTracker
        }
    }

    private fun updateActionModeTitle() {
        mActionMode?.title = String.format(
            getString(R.string.title_delete_mode),
            mSelectTracker?.selection?.size() ?: 0,
            mAdapter.itemCount
        )
    }
}
