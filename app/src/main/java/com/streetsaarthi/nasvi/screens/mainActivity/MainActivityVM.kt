package com.streetsaarthi.nasvi.screens.mainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.google.android.material.tabs.TabLayoutMediator
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemMenuBinding
import com.streetsaarthi.nasvi.screens.main.complaintsFeedback.createNew.CreateNew
import com.streetsaarthi.nasvi.screens.main.complaintsFeedback.history.History
import com.streetsaarthi.nasvi.screens.main.dashboard.Dashboard
import com.streetsaarthi.nasvi.screens.main.informationCenter.InformationCenter
import com.streetsaarthi.nasvi.screens.main.membershipDetails.MembershipDetails
import com.streetsaarthi.nasvi.screens.main.notices.allNotices.AllNotices
import com.streetsaarthi.nasvi.screens.main.notices.liveNotices.LiveNotices
import com.streetsaarthi.nasvi.screens.main.notifications.Notifications
import com.streetsaarthi.nasvi.screens.main.profiles.Profiles
import com.streetsaarthi.nasvi.screens.main.schemes.allSchemes.AllSchemes
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemes
import com.streetsaarthi.nasvi.screens.main.settings.Settings
import com.streetsaarthi.nasvi.screens.main.training.allTraining.AllTraining
import com.streetsaarthi.nasvi.screens.main.training.liveTraining.LiveTraining
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.navHostFragment
import com.streetsaarthi.nasvi.screens.mainActivity.menu.ItemChildMenuModel
import com.streetsaarthi.nasvi.screens.mainActivity.menu.ItemMenuModel
import com.streetsaarthi.nasvi.screens.mainActivity.menu.JsonHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(private val repository: Repository): ViewModel() {

    val locale: Locale = Locale.getDefault()

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
                val headlineAdapter = ChildMenuAdapter(binding.root.context, dataClass.titleChildArray, position)
                recyclerViewChild.adapter = headlineAdapter
                recyclerViewChild.layoutManager = LinearLayoutManager(binding.root.context)

                ivArrow.setOnClickListener {
                        dataClass.isExpanded = !dataClass.isExpanded!!
                        val list = currentList
                        notifyItemRangeChanged(position, list.size)
                }

                root.setOnClickListener {
                        var fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
                        when(position) {
                            0 -> {
                                if (fragmentInFrame !is Dashboard){
                                    navHostFragment?.navController?.navigate(R.id.dashboard)
                                }
                            }
                            1 -> {
                                if (fragmentInFrame !is Profiles){
                                    navHostFragment?.navController?.navigate(R.id.profiles)
                                }
                            }
                            2 -> {
                                if (fragmentInFrame !is Notifications){
                                    navHostFragment?.navController?.navigate(R.id.notifications)
                                }
                            }
                            3 -> {
                                if (fragmentInFrame !is MembershipDetails){
                                    navHostFragment?.navController?.navigate(R.id.membershipDetails)
                                }
                            }
                            8 -> {
                                if (fragmentInFrame !is InformationCenter){
                                    navHostFragment?.navController?.navigate(R.id.informationCenter)
                                }
                            }
                            9 -> {
                                if (fragmentInFrame !is Settings){
                                    navHostFragment?.navController?.navigate(R.id.settings)
                                }
                            }
                        }
                        MainActivity.binding.drawerLayout.close()

                }
            }
        }
    }


    class ChildMenuAdapter(context: Context, data: List<ItemChildMenuModel>?, mainPosition: Int) :
        RecyclerView.Adapter<ChildMenuAdapter.ChildViewHolder>() {
        private var items: List<ItemChildMenuModel>? = data
        private var inflater: LayoutInflater = LayoutInflater.from(context)
        private var parentPosition: Int = mainPosition

        override
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
            val view = inflater.inflate(R.layout.item_child_menu, parent, false)
            return ChildViewHolder(view)
        }

        override
        fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
            val item = items?.get(position)
            holder.tvTitle.text = item?.title
            holder.itemView.setOnClickListener {
                    var fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
                    when(parentPosition) {
                        4 -> when(position) {
                            0 -> {
                                if (fragmentInFrame !is LiveSchemes){
                                    navHostFragment?.navController?.navigate(R.id.liveSchemes)
                                }
                            }
                            1 -> {
                                if (fragmentInFrame !is AllSchemes){
                                    navHostFragment?.navController?.navigate(R.id.allSchemes)
                                }
                            }
                        }
                        5 -> when(position) {
                            0 -> {
                                if (fragmentInFrame !is LiveNotices){
                                    navHostFragment?.navController?.navigate(R.id.liveNotices)
                                }
                            }
                            1 -> {
                                if (fragmentInFrame !is AllNotices){
                                    navHostFragment?.navController?.navigate(R.id.allNotices)
                                }
                            }
                        }
                        6 -> when(position) {
                            0 -> {
                                if (fragmentInFrame !is LiveTraining){
                                    navHostFragment?.navController?.navigate(R.id.liveTraining)
                                }
                            }
                            1 -> {
                                if (fragmentInFrame !is AllTraining){
                                    navHostFragment?.navController?.navigate(R.id.allTraining)
                                }
                            }
                        }
                        7 -> when(position) {
                            0 -> {
                                if (fragmentInFrame !is CreateNew){
                                    navHostFragment?.navController?.navigate(R.id.createNew)
                                }
                            }
                            1 -> {
                                if (fragmentInFrame !is History){
                                    navHostFragment?.navController?.navigate(R.id.history)
                                }
                            }
                        }
                    }
                    MainActivity.binding.drawerLayout.close()
                    Log.e("TAG", ""+parentPosition +" SSS "+position)
            }
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