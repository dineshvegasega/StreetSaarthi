package com.streetsaarthi.screens.main.settings

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(private val repository: Repository): ViewModel() {
}