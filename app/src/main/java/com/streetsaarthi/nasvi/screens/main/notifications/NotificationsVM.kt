package com.streetsaarthi.nasvi.screens.main.notifications

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomInformationCenterBinding
import com.streetsaarthi.nasvi.databinding.ItemInformationCenterBinding
import com.streetsaarthi.nasvi.databinding.ItemNotificationsBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsVM @Inject constructor(private val repository: Repository): ViewModel() {
    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemNotificationsBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemNotificationsBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemNotificationsBinding, dataClass: String, position: Int) {
            binding.apply {
                textTitle.text = HtmlCompat.fromHtml(root.context.getString(R.string.venderDetails, "2024-01-09 11:52 AM"), HtmlCompat.FROM_HTML_MODE_LEGACY);
                val typeface: Typeface? = ResourcesCompat.getFont(root.context, R.font.roboto_medium)
                textTitle.typeface = typeface
                root.setOnClickListener {
                }
            }
        }
    }


}