package com.streetsaarthi.screens.main.informationCenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.databinding.InformationCenterBinding
import com.streetsaarthi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationCenter : Fragment() {
    private val viewModel: InformationCenterVM by viewModels()
    private var _binding: InformationCenterBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InformationCenterBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}