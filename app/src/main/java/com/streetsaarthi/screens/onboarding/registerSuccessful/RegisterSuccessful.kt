package com.streetsaarthi.screens.onboarding.registerSuccessful

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.R
import com.streetsaarthi.databinding.RegisterSuccessfulBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterSuccessful : Fragment() {
    private var _binding: RegisterSuccessfulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterSuccessfulBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btSignIn.setOnClickListener {
                 requireView().findNavController().navigate(R.id.action_registerSuccessful_to_loginPassword)
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}