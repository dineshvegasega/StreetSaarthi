package com.streetsaarthi.screens.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ItemMenuBinding
import com.streetsaarthi.screens.main.MainActivity.Companion.navHostFragment
import com.streetsaarthi.screens.main.menu.ItemChildMenuModel
import com.streetsaarthi.screens.main.menu.ItemMenuModel
import com.streetsaarthi.screens.main.menu.JsonHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(private val repository: Repository): ViewModel() {

    val locale: Locale = MainActivity.context.get()!!.resources.configuration.locales[0]

    var itemMain : List<ItemMenuModel> ?= ArrayList()
    init {
        itemMain = JsonHelper(MainActivity.context.get()!!).getMenuData(locale)
    }

    val menuAdapter = object : GenericAdapter<ItemMenuBinding, ItemMenuModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemMenuBinding.inflate(inflater, parent, false)

        @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
        override fun onBindHolder(binding: ItemMenuBinding, dataClass: ItemMenuModel, position: Int) {
            binding.apply {
                title.text = dataClass.title
                if(dataClass.titleChildArray!!.isEmpty()){
                    ivArrow .visibility = View.GONE
                }else{
                    ivArrow .visibility = View.VISIBLE
                }

                ivArrow.setImageResource(if (dataClass.isExpanded == true) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
                recyclerViewChild.visibility = if (dataClass.isExpanded == true) View.VISIBLE else View.GONE

                recyclerViewChild.setHasFixedSize(true)
                val headlineAdapter = ChildMenuAdapter(binding.root.context, dataClass.titleChildArray)
                recyclerViewChild.adapter = headlineAdapter
                recyclerViewChild.layoutManager = LinearLayoutManager(binding.root.context)

                ivArrow.setOnClickListener {
                        dataClass.isExpanded = !dataClass.isExpanded!!
                        val list = currentList
                        notifyItemRangeChanged(position, list.size)
                }

                root.setOnClickListener {
                    when(position) {
                        0 -> navHostFragment?.navController?.navigate(R.id.home)
                        1 -> navHostFragment?.navController?.navigate(R.id.profiles)
                        2 -> navHostFragment?.navController?.navigate(R.id.notifications)
                        7 -> navHostFragment?.navController?.navigate(R.id.informationCenter)
                        8 -> navHostFragment?.navController?.navigate(R.id.settings)
                    }
                    MainActivity.binding.drawerLayout.close()
                }
            }
        }
    }


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


//    val menuChildAdapter = object : GenericAdapter<ItemChildMenuBinding, ItemChildMenuModel>() {
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            parent: ViewGroup,
//            viewType: Int
//        ) = ItemChildMenuBinding.inflate(inflater, parent, false)
//
//        @SuppressLint("NotifyDataSetChanged")
//        override fun onBindHolder(binding: ItemChildMenuBinding, dataClass: ItemChildMenuModel, position: Int) {
//            binding.apply {
//                val list = currentList
//                titleChild.text = list[position].title
//                root.setOnClickListener {
//
//                }
//            }
//        }
//    }


}