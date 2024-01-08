package com.streetsaarthi.screens.main.profiles

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ProfilesBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.main.profiles.contactDetails.ContactDetails
import com.streetsaarthi.screens.main.profiles.otherDetails.OtherDetails
import com.streetsaarthi.screens.main.profiles.personalDetails.PersonalDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profiles : Fragment() {
    private val viewModel: ProfilesVM by viewModels()
    private var _binding: ProfilesBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfilesBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.your_Profile)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            var adapter= ProfilePagerAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.adapter=adapter
//            introViewPager.setUserInputEnabled(false)
            adapter.addFragment(PersonalDetails())
            adapter.addFragment(ContactDetails())
            adapter.addFragment(OtherDetails())
            var array = listOf<String>(getString(R.string.personal_detailsFull), getString(R.string.contact_details), getString(R.string.other_details))
            TabLayoutMediator(tabLayout, introViewPager) { tab, position ->
                tab.text = array[position]
            }.attach()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}