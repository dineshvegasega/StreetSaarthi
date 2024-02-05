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
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.imageZoom
import com.streetsaarthi.nasvi.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profiles : Fragment() , CallBackListener {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: ProfilesBinding? = null
    private val binding get() = _binding!!

    companion object{
        var callBackListener: CallBackListener? = null
    }

    lateinit var adapter : ProfilePagerAdapter


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
        MainActivity.mainActivity.get()?.callFragment(0)
        callBackListener = this

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.your_Profile)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
            inclideHeaderSearch.textHeaderEditTxt.setOnClickListener {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.INVISIBLE
                btSave.visibility = View.VISIBLE
                btCancel.visibility = View.VISIBLE
                viewModel.isEditable.value = true
            }

            btSave.setOnClickListener {
                PersonalDetails.callBackListener!!.onCallBack(1)
                viewModel.isEditable.value = false
            }

            btCancel.setOnClickListener {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE
                viewModel.isEditable.value = false
            }


            adapter= ProfilePagerAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.isUserInputEnabled = false
            adapter.addFragment(PersonalDetails())
            adapter.addFragment(ProfessionalDetails())

            Handler(Looper.getMainLooper()).postDelayed({
                introViewPager.adapter=adapter
                var array = listOf<String>(getString(R.string.personal_detailsFull), getString(R.string.professional_details))
                TabLayoutMediator(tabLayout, introViewPager) { tab, position ->
                    tab.text = array[position]
                }.attach()
            }, 100)


            updateData()


        }
    }

    private fun updateData() {
        binding.apply {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    Log.e("TAG","loginUser "+loginUser)
                    var data = Gson().fromJson(loginUser, Login::class.java)
                    data.profile_image_name?.let {
                        inclidePersonalProfile.ivImageProfile.loadImage(url = { data.profile_image_name.url })
                        inclidePersonalProfile.ivImageProfile.setOnClickListener {
                            data.profile_image_name?.let {
                                arrayListOf(it.url).imageZoom(inclidePersonalProfile.ivImageProfile)
                            }
                        }
                    }
                    inclidePersonalProfile.textNameOfMember.text = "${data.vendor_first_name} ${data.vendor_last_name}"
                    inclidePersonalProfile.textMobileNumber.text = "+91-${data.mobile_no}"
                    inclidePersonalProfile.textMembershipIdValue.text = "${data.member_id}"
                    data.validity_to?.let {
                        inclidePersonalProfile.textValidUptoValue.text = "${data.validity_to.changeDateFormat("yyyy-MM-dd", "dd-MMM-yyyy")}"
                    }
                    MainActivity.mainActivity.get()!!.callBack()
                }
            }
        }
    }


    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack " + pos)
        if (pos == 11){
//            binding.introViewPager.setCurrentItem(1, false)
            Handler(Looper.getMainLooper()).postDelayed({
                ProfessionalDetails.callBackListener!!.onCallBack(22)
            }, 100)
        }else if (pos == 33){
            binding.apply {
                inclideHeaderSearch.textHeaderEditTxt.visibility = View.VISIBLE
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE

                updateData()
            }
        }
    }
    override fun onDestroyView() {
        _binding = null
//        viewModel.itemState.clear()
//        viewModel.itemDistrict.clear()
//        viewModel.itemPanchayat.clear()
//        viewModel.itemPincode.clear()
//        viewModel.itemStateVending.clear()
//        viewModel.itemDistrictVending.clear()
//        viewModel.itemPanchayatVending.clear()
//        viewModel.itemPincodeVending.clear()
//        viewModel.itemLocalOrganizationVending.clear()
        super.onDestroyView()
    }
}