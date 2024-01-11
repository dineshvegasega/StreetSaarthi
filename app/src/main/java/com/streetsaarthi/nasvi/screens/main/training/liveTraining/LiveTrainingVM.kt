package com.streetsaarthi.nasvi.screens.main.training.liveTraining

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveTrainingBinding
import com.streetsaarthi.nasvi.databinding.ItemLiveTrainingBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LiveTrainingVM @Inject constructor(private val repository: Repository): ViewModel() {
    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemLiveTrainingBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemLiveTrainingBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemLiveTrainingBinding, dataClass: String, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    val dialogBinding = DialogBottomLiveTrainingBinding.inflate(root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
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