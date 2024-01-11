package com.streetsaarthi.nasvi.screens.main.membershipDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.MembershipDetailsBinding
import com.streetsaarthi.nasvi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MembershipDetails  : Fragment() {
    private val viewModel: MembershipDetailsVM by viewModels()
    private var _binding: MembershipDetailsBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MembershipDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.membership_details)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}