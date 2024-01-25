package com.streetsaarthi.nasvi.utils

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class MaxHeightRecyclerView : RecyclerView {


    private var emptyStateView : View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context , attrs: AttributeSet) : super(context,attrs)
    constructor(context: Context , attrs: AttributeSet, defstyle: Int) : super(context,attrs,defstyle)


    var observer: AdapterDataObserver = object : AdapterDataObserver() {

        override fun onChanged() {
            super.onChanged()
            initEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            initEmptyView()
        }
    }

    private fun initEmptyView() {
        emptyStateView?.let {
            it.visibility = if (adapter == null || adapter!!.itemCount == 0) View.VISIBLE else View.GONE
            this@MaxHeightRecyclerView.visibility = if (adapter == null || adapter!!.itemCount == 0) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)
        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }



    /**
     * @param emptyView is the view which is going to display when the recycler view is empty
     * **/
    fun setEmptyView(emptyView: View) {
        this.emptyStateView = emptyView
        initEmptyView()
    }}