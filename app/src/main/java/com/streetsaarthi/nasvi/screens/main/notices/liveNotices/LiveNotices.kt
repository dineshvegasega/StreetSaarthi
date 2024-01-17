package com.streetsaarthi.nasvi.screens.main.notices.liveNotices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.databinding.LiveNoticesBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.main.dashboard.DashboardVM
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveNotices : Fragment() {
    private val viewModel: LiveNoticesVM by viewModels()
    private var _binding: LiveNoticesBinding? = null
    private val binding get() = _binding!!

    companion object{
        var isReadLiveNotices: Boolean? = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LiveNoticesBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isReadLiveNotices = true
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.live_notices)

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