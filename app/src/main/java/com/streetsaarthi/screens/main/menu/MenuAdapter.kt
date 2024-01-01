package com.streetsaarthi.screens.main.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.R


class MenuAdapter(context: Context, data: List<ItemMenuModel>?) :
    RecyclerView.Adapter<MenuAdapter.NewspaperViewHolder>() {
    private var mContext: Context = context
    private var items: List<ItemMenuModel>? = data
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var headlineAdapter: ChildMenuAdapter? = null

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewspaperViewHolder {
        val view = inflater.inflate(R.layout.item_menu, parent, false)
        return NewspaperViewHolder(view)
    }

    override
    fun onBindViewHolder(holder: NewspaperViewHolder, position: Int) {
        val item = items?.get(position)

//        holder.tvName.text = item?.title
//        headlineAdapter = ChildMenuAdapter(mContext, item?.titleChileArray)
//        holder.rvHeadlines.adapter = headlineAdapter
//        holder.rvHeadlines.layoutManager = LinearLayoutManager(mContext)
//        holder.ivArrow.setOnClickListener { onItemClicked(item) }
//        if (item?.isExpanded!!) {
//            holder.rvHeadlines.visibility = View.VISIBLE
//            holder.ivArrow.setImageResource(R.drawable.ic_arrow_up)
//        } else {
//            holder.rvHeadlines.visibility = View.GONE
//            holder.ivArrow.setImageResource(R.drawable.ic_arrow_down)
//        }
    }

    override
    fun getItemCount(): Int {
        return items?.size ?: 0
    }

    private fun onItemClicked(newspaperModel: ItemMenuModel?) {
        newspaperModel?.isExpanded = !newspaperModel?.isExpanded!!
        notifyDataSetChanged()
    }

    class NewspaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var tvName: TextView = itemView.findViewById(R.id.tvPaperName)
//        var rvHeadlines: RecyclerView = itemView.findViewById(R.id.rvHeadlines)
//        var ivArrow: ImageView = itemView.findViewById(R.id.ivArrow)
    }
}