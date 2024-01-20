package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.R

import com.streetsaarthi.nasvi.databinding.ItemLiveSchemesBinding
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.models.mix.ItemLiveScheme
import com.streetsaarthi.nasvi.screens.interfaces.PaginationAdapterCallback
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveSchemeBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.GlideApp
import com.streetsaarthi.nasvi.utils.myOptionsGlide

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/28/2020 - 3:12 PM
 */
class LiveSchemesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback {

    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var itemModels: MutableList<ItemLiveScheme> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemLiveSchemesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_live_schemes, parent, false)
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
            myOrderVH.bind(model)
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


    class TopMoviesVH(binding: ItemLiveSchemesBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLiveSchemesBinding = binding
        fun bind(obj: Any?) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            var dataClass = obj as ItemLiveScheme
            itemRowBinding.apply {
                GlideApp.with(itemRowBinding.root.context)
                    .load(dataClass.scheme_image.url)
                    .apply(myOptionsGlide)
                    .into(ivMap)
                textTitle.setText(dataClass.name)
                textDesc.setText(dataClass.description)
                textHeaderTxt4.setText(dataClass.status)

                root.setOnClickListener {
                    val dialogBinding = DialogBottomLiveSchemeBinding.inflate(root.context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    dialogBinding.apply {
                        val dialog = BottomSheetDialog(root.context)
                        dialog.setContentView(root)
                        dialog.setOnShowListener { dia ->
                            val bottomSheetDialog = dia as BottomSheetDialog
                            val bottomSheetInternal: FrameLayout =
                                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                        }
                        dialog.show()

                        GlideApp.with(root.context)
                            .load(dataClass.scheme_image.url)
                            .apply(myOptionsGlide)
                            .into(ivMap)
                        textTitle.setText(dataClass.name)
                        textDesc.setText(dataClass.description)
                        textHeaderTxt4.setText(dataClass.status)


                        btClose.setOnClickListener {
                            dialog.dismiss()
                        }
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

        val position: Int =itemModels.size -1
        val movie: ItemLiveScheme = itemModels[position]

        if(movie != null){
            itemModels.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}