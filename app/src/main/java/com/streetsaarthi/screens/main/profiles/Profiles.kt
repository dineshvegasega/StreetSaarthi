package com.streetsaarthi.screens.main.profiles

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ProfilesBinding
import com.streetsaarthi.datastore.DataStoreKeys
import com.streetsaarthi.datastore.DataStoreUtil
import com.streetsaarthi.models.Item
import com.streetsaarthi.models.login.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profiles : Fragment() {
    private val viewModel: ProfilesVM by activityViewModels()
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


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.your_Profile)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            var adapter= ProfilePagerAdapter(requireActivity())
            adapter.notifyDataSetChanged()
//            introViewPager.setUserInputEnabled(false)

            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    Log.e("TAG","loginUser "+loginUser)
                    introViewPager.adapter=adapter
                    adapter.addFragment(PersonalDetails())
                    adapter.addFragment(ContactDetails())
                    adapter.addFragment(OtherDetails())
                    var array = listOf<String>(getString(R.string.personal_detailsFull), getString(R.string.contact_details), getString(R.string.other_details))
                    TabLayoutMediator(tabLayout, introViewPager) { tab, position ->
                        tab.text = array[position]
                    }.attach()


                    var data = Gson().fromJson(loginUser, Login::class.java)
                    Picasso.get().load(
                        data.profile_image_name.url
                    ).into( inclidePersonalProfile.ivImageProfile)
                    inclidePersonalProfile.textNameOfMember.text = "${data.vendor_first_name} ${data.vendor_last_name}"
                    inclidePersonalProfile.textMobileNumber.text = "${data.mobile_no}"
                    inclidePersonalProfile.textMembershipIdValue.text = "${data.member_id}"
                    inclidePersonalProfile.textValidUptoValue.text = "${data.validity_to}"
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}