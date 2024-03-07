package com.streetsaarthi.nasvi.screens.main.schemes.allSchemes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.models.ItemLiveScheme
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.glideImagePortrait
import com.streetsaarthi.nasvi.utils.singleClick


class AllSchemesAdapter(liveSchemesVM: AllSchemesVM) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var viewModel = liveSchemesVM
    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var itemModels: MutableList<ItemLiveScheme> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemAllSchemesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_all_schemes, parent, false)
            TopMoviesVH(binding)
        }else{
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_loading, parent, false)
            LoadingVH(binding)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemModels[position]
        if(getItemViewType(position) == item){
            val myOrderVH: TopMoviesVH = holder as TopMoviesVH
//            myOrderVH.itemRowBinding.movieProgress.visibility = View.VISIBLE
            myOrderVH.bind(model, viewModel, position)
        }else{
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE
            } else {
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
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



    class TopMoviesVH(binding: ItemAllSchemesBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemAllSchemesBinding = binding
        fun bind(obj: Any?, viewModel: AllSchemesVM, position: Int) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            var dataClass = obj as ItemLiveScheme
            itemRowBinding.apply {
                dataClass.scheme_image?.url?.glideImagePortrait(itemRowBinding.root.context, ivIcon)
                textTitle.setText(dataClass.name)
                textDesc.setText(dataClass.description)

                textHeaderTxt4.setText(if (dataClass.status == "Active") root.context.resources.getString(R.string.active) else root.context.resources.getString(R.string.expired))
                textHeaderTxt4.backgroundTintList = if (dataClass.status == "Active") ContextCompat.getColorStateList(root.context,R.color._138808) else ContextCompat.getColorStateList(root.context,R.color._F02A2A)


                textStatusValueTxt.setText(if (dataClass.user_scheme_status == "applied") root.context.resources.getString(R.string.applied) else root.context.resources.getString(R.string.not_applied))
                textStatusValueTxt.setTextColor(if(dataClass.user_scheme_status == "applied") ContextCompat.getColorStateList(root.context,R.color._138808) else ContextCompat.getColorStateList(root.context,R.color._F02A2A))

                dataClass.end_at?.let {
                    textValidDateValue.text = "${dataClass.end_at.changeDateFormat("yyyy-MM-dd", "dd MMM, yyyy")}"
                }

                root.singleClick {
                    if(networkFailed) {
                        if (dataClass.user_scheme_status == "applied"){
                            viewModel.viewDetail(dataClass, position = position, root, 1)
                        }else{
                            viewModel.viewDetail(dataClass, position = position, root, 2)
                        }
                    } else {
                        root.context.callNetworkDialog()
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
    fun addAllSearch(movies: MutableList<ItemLiveScheme>) {
        itemModels.clear()
        itemModels.addAll(movies)
//        for(movie in movies){
//            add(movie)
//        }
        notifyDataSetChanged()
    }

    fun addAll(movies: MutableList<ItemLiveScheme>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: ItemLiveScheme) {
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

}