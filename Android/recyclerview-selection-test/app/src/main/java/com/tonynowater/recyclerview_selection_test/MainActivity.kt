package com.tonynowater.recyclerview_selection_test

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.reccyclerview
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var mSelectTracker: SelectionTracker<Long>? = null

    private lateinit var mAdapter: MyListAdapter

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
                mSelectTracker?.clearSelection()
                true
            }
            R.id.action_delete -> {
                mSelectTracker?.selection?.sortedDescending()?.forEach {
                    mAdapter.removeAt(it.toInt())
                    mAdapter.notifyDataSetChanged()
                }
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
                                // TODO: 2019-05-14
                            } else {
                                // TODO: 2019-05-14
                            }
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
}
