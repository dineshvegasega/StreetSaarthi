package com.streetsaarthi.screens.main.informationCenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.R
import com.streetsaarthi.databinding.DialogBottomInformationCenterBinding
import com.streetsaarthi.databinding.DialogBottomLiveSchemeBinding
import com.streetsaarthi.databinding.ItemInformationCenterBinding
import com.streetsaarthi.databinding.ItemLiveSchemesBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationCenterVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemInformationCenterBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemInformationCenterBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemInformationCenterBinding, dataClass: String, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    val dialogBinding = DialogBottomInformationCenterBinding.inflate(root.context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    )
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

                        btClose.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }
}