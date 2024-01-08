package com.streetsaarthi.screens.main.schemes.allSchemes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.R
import com.streetsaarthi.databinding.AllSchemesBinding
import com.streetsaarthi.databinding.DashboardBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.dashboard.DashboardVM
import com.streetsaarthi.screens.mainActivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllSchemes : Fragment() {
    private val viewModel: AllSchemesVM by viewModels()
    private var _binding: AllSchemesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllSchemesBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.all_schemes)

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