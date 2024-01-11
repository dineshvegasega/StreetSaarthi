package com.streetsaarthi.nasvi.screens.main.membershipDetails

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MembershipDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {
}