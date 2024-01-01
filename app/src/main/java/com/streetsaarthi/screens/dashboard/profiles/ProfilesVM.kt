package com.streetsaarthi.screens.dashboard.profiles

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilesVM @Inject constructor(private val repository: Repository): ViewModel() {
}