package com.streetsaarthi.nasvi.screens.onboarding.forgetPassword

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ForgetPasswordBinding
import com.streetsaarthi.nasvi.databinding.LoginPasswordBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.onboarding.quickRegistration.QuickRegistrationVM
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.isValidPassword
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ForgetPassword : Fragment() , OtpTimer.SendOtpTimerData {
    private var _binding: ForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgetPasswordVM by viewModels()

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ForgetPasswordBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OtpTimer.sendOtpTimerData = this


        binding.apply {
            editTextVeryfyOtp.isEnabled = false
            btSignIn.isEnabled = false
            textBack.setOnClickListener {
                view.findNavController().navigateUp()
            }


            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    OtpTimer.startTimer()
                    editTextVeryfyOtp.setEnabled(true)
                    editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }else{
                    editTextVeryfyOtp.setEnabled(false)
                    editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                }
            })

            var counter = 0
            var start: Int
            var end: Int
            imgCreatePassword.setOnClickListener {
                if(counter == 0){
                    counter = 1
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_open)
                    start=editTextPassword.getSelectionStart()
                    end=editTextPassword.getSelectionEnd()
                    editTextPassword.setTransformationMethod(null)
                    editTextPassword.setSelection(start,end)
                }else{
                    counter = 0
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_closed)
                    start=editTextPassword.getSelectionStart()
                    end=editTextPassword.getSelectionEnd()
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextPassword.setSelection(start,end)
                }
            }



            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
                if (it == true){
                    editTextVeryfyOtp.setEnabled(true)
                    editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }else{
                    editTextVeryfyOtp.setEnabled(false)
                    editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                }
            })


            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    editTextSendOtp.setEnabled(false)
                    editTextVeryfyOtp.setEnabled(false)
                    editTextSendOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                    editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))

                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }
            })



            editTextSendOtp.setOnClickListener {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                }else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", binding.editTextMobileNumber.text.toString())
                        put("slug", "login")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.sendOTP(view = requireView(), obj)
                }
            }


            editTextVeryfyOtp.setOnClickListener {
                if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                }else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", binding.editTextMobileNumber.text.toString())
                        put("otp", binding.editTextOtp.text.toString())
                        put("slug", "login")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.verifyOTP(view = requireView(), obj)
                }
            }



            btSignIn.setOnClickListener {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
//                } else if (binding.editTextOtp.text.toString().isEmpty()){
//                    showSnackBar(getString(R.string.enterOtp))
                } else if (editTextPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.YourNewPassword))
                } else if(editTextPassword.text.toString().length >= 0 && editTextPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_number", editTextMobileNumber.text.toString())
                        put("password", editTextPassword.text.toString())
                    }
                    viewModel.passwordupdate(view = requireView(), obj)
                }
            }

        }
    }


    override fun otpData(string: String) {
        binding.tvTime.visibility = if (string.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tvTime.text = getString(R.string.the_verify_code_will_expire_in_00_59, string)
        if(string.isEmpty()){
            binding.editTextSendOtp.setText(getString(R.string.resendOtp))
            binding.editTextSendOtp.setEnabled(true)
            binding.editTextSendOtp.setBackgroundTintList(
                ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        getResources(), R.color._E79D46, null)))
        } else {
            binding.editTextSendOtp.setEnabled(false)
            binding.editTextSendOtp.setBackgroundTintList(
                ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        getResources(), R.color._999999, null)))
        }
    }

    override fun onDestroyView() {
        OtpTimer.sendOtpTimerData = null
        OtpTimer.stopTimer()
        _binding = null
        super.onDestroyView()
    }

}