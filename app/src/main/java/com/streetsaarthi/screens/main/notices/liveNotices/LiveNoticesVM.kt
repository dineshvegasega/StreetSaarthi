package com.streetsaarthi.screens.main.notices.liveNotices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.databinding.ItemLiveNoticeBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiveNoticesVM @Inject constructor(private val repository: Repository): ViewModel() {
    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemLiveNoticeBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemLiveNoticeBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemLiveNoticeBinding, dataClass: String, position: Int) {
            binding.root.setOnClickListener {
                val dialogView: View = LayoutInflater.from(binding.root.context)
                    .inflate(R.layout.dialog_bottom_live_notice, null)
                val dialog = BottomSheetDialog(binding.root.context)
                dialog.setContentView(dialogView)
                dialog.show()
                val window=dialog.window
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }
}