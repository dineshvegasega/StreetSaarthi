package com.streetsaarthi.nasvi.screens.onboarding.registerSuccessful

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.RegisterSuccessfulBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
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
        MainActivity.mainActivity.get()?.callFragment(0)
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