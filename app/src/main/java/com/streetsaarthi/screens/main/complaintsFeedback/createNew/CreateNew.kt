package com.streetsaarthi.screens.main.complaintsFeedback.createNew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.R
import com.streetsaarthi.databinding.CreateNewBinding
import com.streetsaarthi.databinding.DashboardBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.dashboard.DashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNew : Fragment() {
    private val viewModel: CreateNewVM by viewModels()
    private var _binding: CreateNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateNewBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.complaintsSlashfeedback)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE
        }
    }
}