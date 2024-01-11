package com.streetsaarthi.nasvi.screens.main.schemes.allSchemes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.nasvi.databinding.ItemDashboardMenusBinding
import com.streetsaarthi.nasvi.screens.main.dashboard.ItemModel
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllSchemesVM @Inject constructor(private val repository: Repository): ViewModel() {

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