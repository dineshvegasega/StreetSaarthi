package com.streetsaarthi.nasvi.screens.main.informationCenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.InformationCenterBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.main.training.liveTraining.LiveTraining
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationCenter : Fragment() {
    private val viewModel: InformationCenterVM by viewModels()
    private var _binding: InformationCenterBinding? = null
    private val binding get() = _binding!!

    companion object{
        var isReadInformationCenter: Boolean? = false
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InformationCenterBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isReadInformationCenter = true
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.information_center)
            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.photosAdapter
//            viewModel.photosAdapter.notifyDataSetChanged()
//            viewModel.photosAdapter.submitList(viewModel.itemMain)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}