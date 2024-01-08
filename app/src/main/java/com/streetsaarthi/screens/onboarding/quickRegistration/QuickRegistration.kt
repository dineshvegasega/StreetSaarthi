package com.streetsaarthi.screens.onboarding.quickRegistration

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.streetsaarthi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.R
import com.streetsaarthi.databinding.QuickRegistrationBinding
import com.streetsaarthi.screens.interfaces.CallBackListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File


@AndroidEntryPoint
class QuickRegistration : Fragment(), CallBackListener {
    private var _binding: QuickRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuickRegistrationVM by activityViewModels()
    var tabPosition: Int = 0;
//    var itemMain : ArrayList<Item> ?= ArrayList()

    companion object{
        var callBackListener: CallBackListener? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuickRegistrationBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this

//        viewModel.isSend.observe(viewLifecycleOwner, Observer {
//            binding.editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
//        })
//
//

//        binding.btSignIn.setOnClickListener {
//            if(viewModel.isOtpVerified == true){
//                view.findNavController().navigate(R.id.action_quickRegistration_to_onboard)
//            } else{
//                showSnackBar(getString(R.string.OTPnotverified))
//            }
//        }

//        onRestartListener = this as OnTabListener


        binding.apply {
            var adapter= QuickRegistrationAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.adapter=adapter
            introViewPager.setUserInputEnabled(false)

            btSignIn.setEnabled(false)

            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
                }
            })



//            var screen = arguments?.getString(Screen)
//            if (screen == QuickRegister){
//                loading.visibility = View.GONE
//            } else if (screen == CompleteRegister){
//                loading.visibility = View.VISIBLE
//            } else if (screen == LoginPassword){
//                // loading.visibility = View.GONE
//            }


            btSignIn.setOnClickListener {
                    if (tabPosition == 0){
                        QuickRegistration1.callBackListener!!.onCallBack(1)
                    } else if (tabPosition == 1){
                        QuickRegistration2.callBackListener!!.onCallBack(3)
                    }
                loadProgress(tabPosition)
            }

            textBack.setOnClickListener {
                Log.e("Selected_PagetabPosition", ""+tabPosition)
                if (tabPosition == 0){
                    view.findNavController().navigateUp()
                } else if (tabPosition == 1){
                    introViewPager.setCurrentItem(0, false)
                    btSignIn.setText(getString(R.string.continues))
                }
                loadProgress(tabPosition)
            }

            introViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabPosition = position
                    Log.e("Selected_Page", position.toString())
//                    if(position == 2) {
//                        mainThread {
//                            delay(1000)
//                            requireView().findNavController().navigate(R.id.action_walkThrough_to_onBoard)
//                        }
//                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
        }
    }


    private fun loadProgress(tabPosition: Int) {
        var forOne = 100 / 2
        var myPro = tabPosition + 1
        var myProTotal = forOne * myPro
        binding.loading.progress = myProTotal
    }

    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack "+pos)
        binding.apply {
            if (pos == 2){
                introViewPager.setCurrentItem(1, false)
                btSignIn.setText(getString(R.string.RegisterNow))
            } else if (pos == 4){
                val obj: JSONObject = JSONObject().apply {
                    put("vendor_first_name", viewModel.data.vendor_first_name)
                    put("vendor_last_name", viewModel.data.vendor_last_name)
                    put("mobile_no", viewModel.data.mobile_no)
                    put("password", viewModel.data.password)
                    put("user_type", USER_TYPE)
                }
                viewModel.register(view = requireView(), obj)
            }
        }
        loadProgress(tabPosition)
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}