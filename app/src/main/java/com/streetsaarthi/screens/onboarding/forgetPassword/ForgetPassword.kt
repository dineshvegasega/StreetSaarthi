package com.streetsaarthi.screens.onboarding.forgetPassword

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
import com.streetsaarthi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ForgetPasswordBinding
import com.streetsaarthi.databinding.LoginPasswordBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.onboarding.quickRegistration.QuickRegistrationVM
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ForgetPassword : Fragment() {
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


        viewModel.isSend.observe(viewLifecycleOwner, Observer {
            binding.editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
        })


        binding.apply {
            editTextVeryfyOtp.isEnabled = false
            btSignIn.isEnabled = false
            textBack.setOnClickListener {
                view.findNavController().navigateUp()
            }




            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                binding.editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
                if (it == true){
                    binding.editTextVeryfyOtp.setEnabled(true)
                    binding.editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }else{
                    binding.editTextVeryfyOtp.setEnabled(false)
                    binding.editTextVeryfyOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                }
            })


            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    editTextSendOtp.setEnabled(false)
                    editTextVeryfyOtp.setEnabled(false)
                    binding.editTextSendOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._999999, null)))
                    binding.editTextVeryfyOtp.setBackgroundTintList(
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
                if (editTextMobileNumber.text.toString().isEmpty()){
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


            binding.editTextVeryfyOtp.setOnClickListener {
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



            binding.btSignIn.setOnClickListener {
                if (binding.editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterMobileNumber))
//                } else if (binding.editTextOtp.text.toString().isEmpty()){
//                    showSnackBar(getString(R.string.enterOtp))
                } else if (binding.editTextPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.YourNewPassword))
                } else if(binding.editTextPassword.text.toString().length >= 0 && binding.editTextPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_number", binding.editTextMobileNumber.text.toString())
                        put("password", binding.editTextPassword.text.toString())
                    }
                    viewModel.passwordupdate(view = requireView(), obj)
                }
            }






        }





    }

}