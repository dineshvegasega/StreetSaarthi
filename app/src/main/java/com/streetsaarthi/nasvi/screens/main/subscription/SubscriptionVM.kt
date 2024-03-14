package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveSchemeBinding
import com.streetsaarthi.nasvi.databinding.DialogBottomSubscriptionBinding
import com.streetsaarthi.nasvi.databinding.ItemSubscriptionHistoryBinding
import com.streetsaarthi.nasvi.genericAdapter.GenericAdapter
import com.streetsaarthi.nasvi.networking.Repository
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<ItemModel> ?= ArrayList()

    init {
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
    }



    val historyAdapter = object : GenericAdapter<ItemSubscriptionHistoryBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSubscriptionHistoryBinding.inflate(inflater, parent, false)

        @SuppressLint("SuspiciousIndentation")
        override fun onBindHolder(binding: ItemSubscriptionHistoryBinding, dataClass: ItemModel, position: Int) {
            binding.apply {
                layoutMain.backgroundTintList = (if(position % 2 == 0) ContextCompat.getColorStateList(root.context,R.color._f6dbbb) else ContextCompat.getColorStateList(root.context,R.color.white))
                textSno.setText(dataClass.s_no)
                textDate.setText(dataClass.date)
                textTransactionId.setText(dataClass.transactionId)
                textDuration.setText(dataClass.duration +" "+ root.resources.getString(R.string.months))
                root.singleClick {
                    val dialogBinding = DialogBottomSubscriptionBinding.inflate(root.context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    )
                    val dialog = BottomSheetDialog(root.context)
                    dialog.setContentView(dialogBinding.root)
                    dialog.setOnShowListener { dia ->
                        val bottomSheetDialog = dia as BottomSheetDialog
                        val bottomSheetInternal: FrameLayout =
                            bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                        bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                    }
                    dialog.show()

                    dialogBinding.apply {
                        imageCross.singleClick {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }
}



data class ItemModel (
    var s_no: String = "01",
    var date: String = "12/02/22",
    var transactionId: String = "#312314",
    var duration: String = "12",

)