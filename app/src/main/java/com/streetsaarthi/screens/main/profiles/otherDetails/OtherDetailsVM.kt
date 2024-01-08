package com.streetsaarthi.screens.main.profiles.otherDetails

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {
}