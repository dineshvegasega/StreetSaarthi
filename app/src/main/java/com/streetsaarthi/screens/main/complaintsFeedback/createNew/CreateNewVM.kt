package com.streetsaarthi.screens.main.complaintsFeedback.createNew

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewVM @Inject constructor(private val repository: Repository): ViewModel() {
}