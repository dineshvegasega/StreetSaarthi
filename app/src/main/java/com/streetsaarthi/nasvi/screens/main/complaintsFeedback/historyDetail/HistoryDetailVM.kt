package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.historyDetail

import androidx.lifecycle.ViewModel
import com.demo.home.HomeAdapter
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryDetailVM @Inject constructor(private val repository: Repository): ViewModel() {
    val chatAdapter by lazy { HomeAdapter() }

    data class ItemModel(
        var color: Int,
        var name: String
    )
}