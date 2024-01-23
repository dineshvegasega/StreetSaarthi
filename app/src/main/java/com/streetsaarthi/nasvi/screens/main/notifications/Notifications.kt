package com.streetsaarthi.nasvi.screens.main.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.NotificationsBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
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


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.notifications)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE
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