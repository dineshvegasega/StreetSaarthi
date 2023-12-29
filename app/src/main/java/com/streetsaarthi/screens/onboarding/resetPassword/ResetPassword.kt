package com.streetsaarthi.screens.onboarding.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.streetsaarthi.databinding.LoginPasswordBinding
import com.streetsaarthi.databinding.ResetPasswordBinding
import com.streetsaarthi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPassword : Fragment() {
    private var _binding: ResetPasswordBinding? = null
    private val binding get() = _binding!!


    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ResetPasswordBinding.inflate(inflater)
        return binding.root
    }
}