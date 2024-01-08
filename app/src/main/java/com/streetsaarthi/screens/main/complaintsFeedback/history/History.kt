package com.streetsaarthi.screens.main.complaintsFeedback.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.R
import com.streetsaarthi.databinding.DashboardBinding
import com.streetsaarthi.databinding.HistoryBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.dashboard.DashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class History : Fragment() {
    private val viewModel: HistoryVM by viewModels()
    private var _binding: HistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.AllComplaintsFeedback)
            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.photosAdapter
            viewModel.photosAdapter.notifyDataSetChanged()
            viewModel.photosAdapter.submitList(viewModel.itemMain)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}