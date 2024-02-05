package com.streetsaarthi.nasvi.screens.main.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.databinding.ItemNotificationsBinding
import com.streetsaarthi.nasvi.models.mix.ItemNotification
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.interfaces.PaginationAdapterCallback
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.changeDateFormat
import java.util.Locale

class NotificationsAdapter (liveSchemesVM: NotificationsVM) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback, CallBackListener {
    var viewModel = liveSchemesVM
    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var itemModels: MutableList<ItemNotification> = ArrayList()

    companion object{
        var callBackListener: CallBackListener? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemNotificationsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notifications, parent, false)
            callBackListener = this

            TopMoviesVH(binding)
        }else{
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_loading, parent, false)
            LoadingVH(binding)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemModels[position]
        if(getItemViewType(position) == item){
            callBackListener = this

            val myOrderVH: TopMoviesVH = holder as TopMoviesVH
//            myOrderVH.itemRowBinding.movieProgress.visibility = View.VISIBLE
            myOrderVH.bind(model, viewModel, position)
        }else{
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = MainActivity.activity.get()?.getString(
                    R.string.error_msg_unknown)

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemModels.size > 0) itemModels.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == itemModels.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun retryPageLoad() {
        // mActivity.loadNextPage()
    }



    class TopMoviesVH(binding: ItemNotificationsBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemNotificationsBinding = binding
        @SuppressLint("SetTextI18n")
        fun bind(obj: Any?, viewModel: NotificationsVM, position: Int) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            var dataClass = obj as ItemNotification
            itemRowBinding.apply {
                textTitle.setText(dataClass.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()) else it.toString() } +" "+ dataClass.sent_at.changeDateFormat("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm a"))

                textDesc.setText(dataClass.title)
//                textHeaderTxt4.setText(dataClass.status)

                root.setOnClickListener {
//                    if (dataClass.user_scheme_status == "applied"){
                    //viewModel.viewDetail(""+dataClass.notice_id, position = position, root, 1)
//                    }else{
//                        viewModel.viewDetail(""+dataClass.scheme_id, position = position, root, 2)
//                    }
                    when(dataClass.type){
                        "VendorDetails" -> root.findNavController().navigate(R.id.action_notifications_to_profile)
                        "scheme" -> root.findNavController().navigate(R.id.action_notifications_to_liveSchemes)
                        "notice" -> root.findNavController().navigate(R.id.action_notifications_to_liveNotices)
                        "training" -> root.findNavController().navigate(R.id.action_notifications_to_liveTraining)
                        "Feedback" -> root.findNavController().navigate(R.id.action_notifications_to_historyDetail, Bundle().apply {
                            putString("key", ""+dataClass.type_id)
                        })
                    }
                }

            }
        }


    }

    class LoadingVH(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(itemModels.size - 1)
        this.errorMsg = errorMsg
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllSearch(movies: MutableList<ItemNotification>) {
        itemModels.clear()
        itemModels.addAll(movies)
//        for(movie in movies){
//            add(movie)
//        }
        notifyDataSetChanged()
    }

    fun addAll(movies: MutableList<ItemNotification>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: ItemNotification) {
        itemModels.add(moive)
        notifyItemInserted(itemModels.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ItemLiveScheme())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

//        val position: Int =itemModels.size -1
//        val movie: ItemLiveScheme = itemModels[position]
//
//        if(movie != null){
//            itemModels.removeAt(position)
//            notifyItemRemoved(position)
//        }
    }

    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack "+pos)
//        onCallBack(pos)

    }
}