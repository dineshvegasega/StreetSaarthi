package com.streetsaarthi.screens.main.schemes.liveSchemes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.databinding.ItemAllSchemesBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiveSchemesVM @Inject constructor(private val repository: Repository): ViewModel() {
    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemAllSchemesBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemAllSchemesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemAllSchemesBinding, dataClass: String, position: Int) {

        }
    }
}