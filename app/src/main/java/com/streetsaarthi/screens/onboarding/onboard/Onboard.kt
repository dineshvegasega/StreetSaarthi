package com.streetsaarthi.screens.onboarding.onboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.streetsaarthi.R
import com.streetsaarthi.databinding.OnboardBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.home.HomeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboard : Fragment() {
    private var _binding: OnboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OnboardBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = viewModel.photosAdapter
        viewModel.photosAdapter.submitList(viewModel.itemMain)

        binding.btSignIn.setEnabled(false)

        viewModel.clickEvent.observe(viewLifecycleOwner, Observer {
            if (it == true){
                binding.btSignIn.setEnabled(true)
                binding.btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            }
        })



        binding.btSignIn.setOnClickListener {
            viewModel.callNextPage(it)
        }

    }



    data class Item (
        var name: String = "",
        var image: Int = 0,
        var isSelected: Boolean? = false
    )
}