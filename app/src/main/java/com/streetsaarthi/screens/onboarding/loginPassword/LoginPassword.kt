package com.streetsaarthi.screens.onboarding.loginPassword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.blacqhorse.customClasses.phoneValidation.PhoneNumberValidator
import com.demo.networking.CompleteRegister
import com.demo.networking.Screen
import com.streetsaarthi.R
import com.streetsaarthi.databinding.LoginPasswordBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber
import org.json.JSONObject


@AndroidEntryPoint
class LoginPassword : Fragment() {
    private val viewModel: LoginPasswordVM by viewModels()
    private var _binding: LoginPasswordBinding? = null
    private val binding get() = _binding!!


    private var util: PhoneNumberUtil? = null

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val obj: JSONObject = JSONObject()
        obj.put("mobile_number", binding.editTextMobileNumber.text.toString())
        obj.put("password", binding.editTextPassword.text.toString())

        if (util == null) {
            util = PhoneNumberUtil.createInstance(requireContext());
        }
//        try {
//            val phoneNumber: PhoneNumber = util!!.parse("", "US")
//            var isValid = util?.isValidNumber(phoneNumber) ?: false
//            Log.e("TAG", "isValid "+isValid)
//
////            textView.setText(
////                util.format(
////                    phoneNumber,
////                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL
////                )
////            )
//        } catch (e: NumberParseException) {
//            e.printStackTrace()
//        }


        binding.apply {
            textBack.setOnClickListener {
                view.findNavController().navigateUp()
            }

            editTextLoginWith.setOnClickListener {
                view.findNavController().navigate(R.id.action_loginPassword_to_loginOtp)
            }

//            var phoneNumberValidator =PhoneNumberValidator(requireContext())
//            var xxx = phoneNumberValidator.checkValidPhoneNumber("+91", "2988397522", "IND")
//
//            Log.e("TAG", "xxx "+xxx)

            binding.btSignIn.setOnClickListener {
                if (binding.editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterMobileNumber))
                } else if (binding.editTextPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.EnterPassword))
                } else {
                    viewModel.login(view = requireView(), obj)
                }
            }


            binding.editTextForgot.setOnClickListener {
                view.findNavController().navigate(R.id.action_loginPassword_to_forgetPassword)
            }

        }

    }


}