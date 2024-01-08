package com.streetsaarthi.screens.main.profiles.contactDetails

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {
}