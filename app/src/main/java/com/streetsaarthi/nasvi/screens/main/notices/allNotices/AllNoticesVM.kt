package com.streetsaarthi.nasvi.screens.main.notices.allNotices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.nasvi.databinding.ItemAllNoticesBinding
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllNoticesVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemAllNoticesBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemAllNoticesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemAllNoticesBinding, dataClass: String, position: Int) {

        }
    }
}