package com.streetsaarthi.nasvi.screens.main.profiles

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ProfilesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.quickRegistration.QuickRegistration
import com.streetsaarthi.nasvi.screens.onboarding.quickRegistration.QuickRegistration1
import com.streetsaarthi.nasvi.utils.GlideApp
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.myOptionsGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profiles : Fragment() , CallBackListener {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: ProfilesBinding? = null
    private val binding get() = _binding!!

    companion object{
        var callBackListener: CallBackListener? = null
    }

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
        callBackListener = this

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.your_Profile)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
            inclideHeaderSearch.textHeaderEditTxt.setOnClickListener {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.INVISIBLE
                btSave.visibility = View.VISIBLE
                btCancel.visibility = View.VISIBLE
            }

            btSave.setOnClickListener {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE
                PersonalDetails.callBackListener!!.onCallBack(1)
            }

            btCancel.setOnClickListener {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE
            }

            var adapter= ProfilePagerAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.setUserInputEnabled(false)

            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    Log.e("TAG","loginUser "+loginUser)
                    introViewPager.adapter=adapter
                    adapter.addFragment(PersonalDetails())
                    adapter.addFragment(ProfessionalDetails())
//                    adapter.addFragment(OtherDetails())
                    var array = listOf<String>(getString(R.string.personal_detailsFull), getString(R.string.professional_details))
                    TabLayoutMediator(tabLayout, introViewPager) { tab, position ->
                        tab.text = array[position]
                    }.attach()


                    var data = Gson().fromJson(loginUser, Login::class.java)
//                    Picasso.get().load(
//                        data.profile_image_name.url
//                    ).into( inclidePersonalProfile.ivImageProfile)

                    inclidePersonalProfile.ivImageProfile.loadImage(url = { data.profile_image_name.url })


//                    GlideApp.with(requireContext())
//                        .load(Gson().fromJson(loginUser, Login::class.java).profile_image_name.url)
//                        .apply(myOptionsGlide)
//                        .into(inclidePersonalProfile.ivImageProfile)

                    inclidePersonalProfile.textNameOfMember.text = "${data.vendor_first_name} ${data.vendor_last_name}"
                    inclidePersonalProfile.textMobileNumber.text = "${data.mobile_no}"
                    inclidePersonalProfile.textMembershipIdValue.text = "${data.member_id}"
                    inclidePersonalProfile.textValidUptoValue.text = "${data.validity_to}"
                }
            }
        }
    }


    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack " + pos)
        if (pos == 11){
            binding.introViewPager.setCurrentItem(1, false)
            Handler(Looper.getMainLooper()).postDelayed({
                ProfessionalDetails.callBackListener!!.onCallBack(22)
            }, 100)
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}