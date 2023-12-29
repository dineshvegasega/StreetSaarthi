package com.streetsaarthi.screens.onboarding.completeRegistration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.streetsaarthi.databinding.CompleteRegistrationBinding
import com.streetsaarthi.databinding.LoginPasswordBinding
import com.streetsaarthi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteRegistration : Fragment() {

    private var _binding: CompleteRegistrationBinding? = null
    private val binding get() = _binding!!


    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CompleteRegistrationBinding.inflate(inflater)
        return binding.root
    }
}