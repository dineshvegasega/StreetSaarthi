package com.streetsaarthi.nasvi.screens.onboarding.loginOtp

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
//import com.stfalcon.smsverifycatcher.OnSmsCatchListener
//import com.stfalcon.smsverifycatcher.SmsVerifyCatcher
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.LoginOtpBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LoginOtp : Fragment() , OtpTimer.SendOtpTimerData {
    private var _binding: LoginOtpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginOtpVM by activityViewModels()


//    private var smsVerifyCatcher: SmsVerifyCatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginOtpBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        OtpTimer.sendOtpTimerData = this

        binding.apply {
//            editTextVeryfyOtp.isEnabled = false
//            btSignIn.isEnabled = false

            textBack.singleClick {
                view.findNavController().navigateUp()
            }

            viewModel.isSend.value = false
            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
                if (it == true){
                    OtpTimer.startTimer()
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }else{
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
                    tvTime.visibility = View.GONE
//                    OtpTimer.sendOtpTimerData = null
                    OtpTimer.stopTimer()
//                    editTextSendOtp.setEnabled(false)
//                    editTextVeryfyOtp.setEnabled(false)
//                    binding.editTextSendOtp.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ResourcesCompat.getColor(
//                                getResources(), R.color._999999, null)))
//                    binding.editTextVeryfyOtp.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ResourcesCompat.getColor(
//                                getResources(), R.color._999999, null)))
//
//                    btSignIn.setEnabled(true)
//                    btSignIn.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ResourcesCompat.getColor(
//                                getResources(), R.color._E79D46, null)))
                }
            })


//            smsVerifyCatcher = SmsVerifyCatcher(requireActivity(),
//                OnSmsCatchListener<String?> { message ->
//                    if(message != null && message.length >= 6){
//                        var otp = message.trim().substring(0,6).toInt()
//                        editTextOtp.setText("${otp}")
//                        var start2=editTextOtp.getSelectionStart()
//                        var end2=editTextOtp.getSelectionEnd()
//                        editTextOtp.setSelection(start2,end2)
//                    }
//                })

            editTextSendOtp.singleClick {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                }else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", editTextMobileNumber.text.toString())
                        put("slug", "login")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.sendOTP(view = requireView(), obj)
                }
            }


            editTextVeryfyOtp.singleClick {
                if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                }else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", editTextMobileNumber.text.toString())
                        put("otp", editTextOtp.text.toString())
                        put("slug", "login")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.verifyOTPData(view = requireView(), obj)
                }
            }



            btSignIn.singleClick {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                } else if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                } else{
//                    isFree = true
//                    callMediaPermissions()
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", binding.editTextMobileNumber.text.toString())
                        put("otp", binding.editTextOtp.text.toString())
                        put("slug", "login")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.verifyOTPData(view = requireView(), obj)
                }
            }



            editTextLoginWith.singleClick {
                view.findNavController().navigate(R.id.action_loginOtp_to_loginPassword)
            }
        }
    }




    private fun callMediaPermissions() {
        activityResultLauncher.launch(
            arrayOf()
        )
    }



    var isFree = false
    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                Log.e("TAG", "00000 "+permissionName)
                if (isGranted) {
                    Log.e("TAG", "11111"+permissionName)
                    if(isFree){
                        val obj: JSONObject = JSONObject().apply {
                            put("mobile_no", binding.editTextMobileNumber.text.toString())
                            put("otp", binding.editTextOtp.text.toString())
                            put("slug", "login")
                            put("user_type", USER_TYPE)
                        }
                        viewModel.verifyOTPData(view = requireView(), obj)
//                        smsVerifyCatcher!!.onStart()
                    }
                    isFree = false
                } else {
                    // Permission is denied
                    Log.e("TAG", "222222"+permissionName)
                }
            }
        }


    var isTimer = ""
    override fun otpData(string: String) {
        isTimer = string
        binding.apply {
            tvTime.visibility = if (string.isNotEmpty()) View.VISIBLE else View.GONE
            tvTime.text = getString(R.string.the_verify_code_will_expire_in_00_59, string)

//            if(MainActivity.isOpen.get() == true){
//                editTextOtp.focus()
//            }

            if(string.isEmpty()){
                editTextSendOtp.setText(getString(R.string.resendOtp))
                editTextSendOtp.setEnabled(true)
                editTextSendOtp.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else {
                editTextSendOtp.setEnabled(false)
                editTextSendOtp.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._999999, null)))
            }
        }

    }

    override fun onDestroyView() {
        OtpTimer.sendOtpTimerData = null
        OtpTimer.stopTimer()
        _binding = null
        super.onDestroyView()
    }
}