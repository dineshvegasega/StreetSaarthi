package com.streetsaarthi.nasvi.screens.main.notices.allNotices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.AllNoticesBinding
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.main.dashboard.DashboardVM
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllNotices : Fragment() {
    private val viewModel: AllNoticesVM by viewModels()
    private var _binding: AllNoticesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllNoticesBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.all_notices)

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