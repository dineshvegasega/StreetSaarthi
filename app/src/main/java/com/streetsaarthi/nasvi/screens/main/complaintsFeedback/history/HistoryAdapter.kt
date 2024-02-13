package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemHistoryBinding
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.models.mix.ItemHistory
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.interfaces.PaginationAdapterCallback
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.glideImage
import com.streetsaarthi.nasvi.utils.glideImagePortrait
import com.streetsaarthi.nasvi.utils.singleClick


class HistoryAdapter(liveSchemesVM: HistoryVM) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback, CallBackListener {
    var viewModel = liveSchemesVM
    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var itemModels: MutableList<ItemHistory> = ArrayList()

    companion object{
        var callBackListener: CallBackListener? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemHistoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history, parent, false)
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

            loadingVH.itemRowBinding.loadmoreRetry.singleClick{
                showRetry(false, "")
                retryPageLoad()
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.singleClick{
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



    class TopMoviesVH(binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemHistoryBinding = binding
        fun bind(obj: Any?, viewModel: HistoryVM, position: Int) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            var dataClass = obj as ItemHistory
            itemRowBinding.apply {
                dataClass.media?.url?.glideImagePortrait(itemRowBinding.root.context, ivIcon)
               var complaintfeedback = if (dataClass.type == "complaint"){
                    root.context.getString(R.string.complaint)
                } else {
                    root.context.getString(R.string.feedback)
                }
                textTitle.text = complaintfeedback

                textDesc.setText(dataClass.subject)
                textTrackValue.setText(""+dataClass.feedback_id)
//                textStatusValueTxt.setText(dataClass.status.titlecaseFirstCharIfItIsLowercase())

                textStatusValueTxt.setText(
                    if (dataClass.status == "in-progress") root.context.getString(R.string.in_progress)
                    else if (dataClass.status == "Pending" || dataClass.status == "pending") root.context.getString(R.string.pending)
                    else if (dataClass.status == "resolved") root.context.getString(R.string.resolved)
                    else if (dataClass.status == "re-open") root.context.getString(R.string.re_open)
                    else root.context.getString(R.string.pending))

                textStatusValueTxt.setTextColor(
                    if (dataClass.status == "in-progress") ContextCompat.getColor(root.context, R.color._E79D46)
                else if (dataClass.status == "Pending" || dataClass.status == "pending") ContextCompat.getColor(root.context, R.color.black)
                else if (dataClass.status == "resolved") ContextCompat.getColor(root.context, R.color._138808)
                else if (dataClass.status == "re-open") ContextCompat.getColor(root.context, R.color._ED2525)
                else ContextCompat.getColor(root.context, R.color._ffffffff))

                dataClass.date?.let {
                    textValidDateValue.text = "${dataClass.date.changeDateFormat("dd-MM-yyyy", "dd MMM, yyyy")}"
                }
                root.singleClick {
                    view.findNavController().navigate(R.id.action_history_to_historyDetail, Bundle().apply {
                        putString("key", ""+dataClass.feedback_id)
                    })
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
    fun addAllSearch(movies: MutableList<ItemHistory>) {
        itemModels.clear()
        itemModels.addAll(movies)
//        for(movie in movies){
//            add(movie)
//        }
        notifyDataSetChanged()
    }

    fun addAll(movies: MutableList<ItemHistory>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: ItemHistory) {
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