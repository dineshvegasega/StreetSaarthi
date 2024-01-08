package com.streetsaarthi.screens.main.profiles.personalDetails

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {
}