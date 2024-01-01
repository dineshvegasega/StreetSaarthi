package com.streetsaarthi.screens.main.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.R


class ChildMenuAdapter(context: Context, data: List<ItemChildMenuModel>?) :
    RecyclerView.Adapter<ChildMenuAdapter.ChildViewHolder>() {
    private var items: List<ItemChildMenuModel>? = data
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = inflater.inflate(R.layout.item_child_menu, parent, false)
        return ChildViewHolder(view)
    }

    override
    fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item = items?.get(position)

        holder.tvTitle.text = item?.title
    }

    override
    fun getItemCount(): Int {
        return items?.size?:0
    }

    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: AppCompatTextView = itemView.findViewById(R.id.titleChild)
    }
}