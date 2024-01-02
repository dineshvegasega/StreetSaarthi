package com.streetsaarthi.screens.main.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ListItemBinding
//import com.streetsaarthi.models.Item
import com.streetsaarthi.models.Items
import com.squareup.picasso.Picasso
import com.streetsaarthi.databinding.ItemDashboardMenusBinding
import com.streetsaarthi.databinding.ItemRecentActivitiesBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.complaintsFeedback.createNew.CreateNew
import com.streetsaarthi.screens.main.complaintsFeedback.history.History
import com.streetsaarthi.screens.main.notices.allNotices.AllNotices
import com.streetsaarthi.screens.main.notices.liveNotices.LiveNotices
import com.streetsaarthi.screens.main.schemes.allSchemes.AllSchemes
import com.streetsaarthi.screens.main.schemes.liveSchemes.LiveSchemes
import com.streetsaarthi.screens.main.training.allTraining.AllTraining
import com.streetsaarthi.screens.main.training.liveTraining.LiveTraining
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.screens.mainActivity.MainActivityVM
import com.streetsaarthi.screens.mainActivity.menu.ItemChildMenuModel
import com.streetsaarthi.screens.onboarding.onboard.Onboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.RawValue
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(private val repository: Repository): ViewModel() {


    var itemMain : ArrayList<ItemModel> ?= ArrayList()
    init {
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.profiles),
                R.drawable.item_profile
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.live_schemes),
                R.drawable.item_live_scheme
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.live_notices),
                R.drawable.item_live_notices
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.live_training),
                R.drawable.item_live_training
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.complaints_feedback),
                R.drawable.item_feedback
            )
        )
    }

    val photosAdapter = object : GenericAdapter<ItemDashboardMenusBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemDashboardMenusBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemDashboardMenusBinding, dataClass: ItemModel, position: Int) {
//            Picasso.get().load(
//                dataClass
//            ).into(binding!!.ivLogo)
            binding.textHeaderadfdsfTxt.setText(dataClass.name)
            binding.ivLogo.setImageResource(dataClass.image)
            binding.root.setOnClickListener {
                when (position) {
                    0 -> it.findNavController().navigate(R.id.action_dashboard_to_profile)
                    1 -> it.findNavController().navigate(R.id.action_dashboard_to_liveSchemes)
                    2 -> it.findNavController().navigate(R.id.action_dashboard_to_liveNotices)
                    3 -> it.findNavController().navigate(R.id.action_dashboard_to_liveTraining)
                    4 -> it.findNavController().navigate(R.id.action_dashboard_to_createNew)
                }

            }
        }
    }


    val recentAdapter = object : GenericAdapter<ItemRecentActivitiesBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemRecentActivitiesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemRecentActivitiesBinding, dataClass: ItemModel, position: Int) {
            binding.apply {
                recyclerViewRecentItems.setHasFixedSize(true)
                val headlineAdapter = RecentChildAdapter(
                    binding.root.context,
                    dataClass.items,
                    position
                )
                recyclerViewRecentItems.adapter = headlineAdapter
                recyclerViewRecentItems.layoutManager = LinearLayoutManager(binding.root.context)
            }
        }
    }




    class RecentChildAdapter(context: Context, data: List<String>?, mainPosition: Int) :
        RecyclerView.Adapter<RecentChildAdapter.ChildViewHolder>() {
        private var items: List<String>? = data
        private var inflater: LayoutInflater = LayoutInflater.from(context)
        private var parentPosition: Int = mainPosition

        override
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
            val view = inflater.inflate(R.layout.item_recent_activities_items, parent, false)
            return ChildViewHolder(view)
        }

        override
        fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
//            val item = items?.get(position)
            //holder.tvTitle.text = item?.title
            holder.itemView.setOnClickListener {
            }
        }

        override
        fun getItemCount(): Int {
//            return items?.size?:0
            return 3
        }

        class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            var tvTitle: AppCompatTextView = itemView.findViewById(R.id.titleChild)
        }
    }







    private var result = MutableLiveData<Items>()
    val readResult : LiveData<Items> get() = result

//    fun getProducts(query: String?){
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<Items>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        apiInterface.getItems(""+query)
//                    override fun success(response: Response<Items>) {
//                        if (response.isSuccessful){
//                            result.value = response.body()
//                        }
//                    }
//
//                    override fun error(message: String) {
//                        super.error(message)
//                    }
//
//                    override fun loading() {
//                        super.loading()
//                    }
//                }
//            )
//        }
//
//
//    }


}

data class ItemModel (
    var name: String = "",
    var image: Int = 0,
    val items: @RawValue ArrayList<String> ?= ArrayList(),
)