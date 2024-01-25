package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.QuickRegistrationBinding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.OtpTimer
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class QuickRegistration : Fragment(), CallBackListener{
    private var _binding: QuickRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuickRegistrationVM by activityViewModels()
    var tabPosition: Int = 0;

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
//        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)

        MainActivity.mainActivity.get()?.callFragment(0)
        callBackListener = this
        binding.apply {
            var adapter= QuickRegistrationAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.adapter=adapter
            introViewPager.setUserInputEnabled(false)

            btSignIn.setEnabled(false)


            viewModel.isAgree.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    Log.e("TAG", "isAgreeAA "+viewModel.isAgree.value)
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                } else {
                    Log.e("TAG", "isAgreeBB "+viewModel.isAgree.value)
                    btSignIn.setEnabled(false)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                }
            })

            viewModel.isSendMutable.value = false
            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    Log.e("TAG", "isSendMutableAA "+viewModel.isSendMutable.value)
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
                }
            })


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
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
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
//                    if(position == 0) {
//                        btSignIn.setEnabled(true)
//                        btSignIn.setBackgroundTintList(
//                            ColorStateList.valueOf(
//                                ResourcesCompat.getColor(
//                                    getResources(), R.color._E79D46, null)))
//                    }
                    if(position == 1) {
                        btSignIn.setEnabled(false)
                        btSignIn.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ResourcesCompat.getColor(
                                    getResources(), R.color._999999, null)))
                    }
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
          if (pos == 21){
            btSignIn.setEnabled(false)
            btSignIn.setBackgroundTintList(
                ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        getResources(), R.color._999999, null)))
        } else if (pos == 2){
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
//        binding.apply {
//            btSignIn.setEnabled(false)
//            btSignIn.setBackgroundTintList(
//                ColorStateList.valueOf(
//                    ResourcesCompat.getColor(
//                        getResources(), R.color._999999, null)))
//        }
        viewModel.isAgree.value = false
        OtpTimer.sendOtpTimerData = null
        OtpTimer.stopTimer()
        _binding = null
        super.onDestroyView()
    }
}