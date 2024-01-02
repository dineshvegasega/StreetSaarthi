package com.streetsaarthi.screens.main.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.databinding.NotificationsBinding
import com.streetsaarthi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notifications : Fragment() {
    private val viewModel: NotificationsVM by viewModels()
    private var _binding: NotificationsBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotificationsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}