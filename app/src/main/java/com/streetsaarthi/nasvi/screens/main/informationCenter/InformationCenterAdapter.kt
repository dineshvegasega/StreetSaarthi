package com.streetsaarthi.nasvi.screens.main.informationCenter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.screens.interfaces.PaginationAdapterCallback
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.databinding.ItemInformationCenterBinding
import com.streetsaarthi.nasvi.models.mix.ItemInformationCenter
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.glideImage

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/28/2020 - 3:12 PM
 */
class InformationCenterAdapter(liveSchemesVM: InformationCenterVM) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback, CallBackListener {
    var viewModel = liveSchemesVM
    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var itemModels: MutableList<ItemInformationCenter> = ArrayList()

    companion object{
        var callBackListener: CallBackListener? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemInformationCenterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_information_center, parent, false)
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
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = MainActivity.activity.get()?.getString(R.string.error_msg_unknown)

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



    class TopMoviesVH(binding: ItemInformationCenterBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemInformationCenterBinding = binding
        @SuppressLint("ResourceAsColor")
        fun bind(obj: Any?, viewModel: InformationCenterVM, position: Int) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            var dataClass = obj as ItemInformationCenter
            itemRowBinding.apply {
                dataClass.cover_image?.url?.glideImage(itemRowBinding.root.context, ivMap)
                textTitle.setText(dataClass.title)
                textDesc.setText(dataClass.description)

//                textHeaderTxt4.setText(if (dataClass.status == "Active") root.context.resources.getString(R.string.live) else root.context.resources.getString(R.string.not_live))
//                textHeaderTxt4.backgroundTintList = (if(dataClass.status == "Active") ContextCompat.getColorStateList(root.context,R.color._138808) else ContextCompat.getColorStateList(root.context,R.color._F02A2A))

                root.setOnClickListener {
//                        if (dataClass.user_scheme_status == "applied"){
                    viewModel.viewDetail(""+dataClass.information_id, position = position, root, 1)
//                        }else{
//                            viewModel.viewDetail(dataClass, position = position, root, 2)
//                        }
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
    fun addAllSearch(movies: MutableList<ItemInformationCenter>) {
        itemModels.clear()
        itemModels.addAll(movies)
//        for(movie in movies){
//            add(movie)
//        }
        notifyDataSetChanged()
    }

    fun addAll(movies: MutableList<ItemInformationCenter>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: ItemInformationCenter) {
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