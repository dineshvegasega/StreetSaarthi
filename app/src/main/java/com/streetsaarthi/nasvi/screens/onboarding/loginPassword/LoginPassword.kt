package com.streetsaarthi.nasvi.screens.onboarding.loginPassword

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.LoginPasswordBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.isValidPassword
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class LoginPassword : Fragment() {
    private val viewModel: LoginPasswordVM by viewModels()
    private var _binding: LoginPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)

//
//        if (util == null) {
//            util = PhoneNumberUtil.createInstance(requireContext());
//        }
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
            textBack.singleClick {
                view.findNavController().navigateUp()
            }

            var counter = 0
            var start: Int
            var end: Int
            imgCreatePassword.singleClick {
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

            editTextLoginWith.singleClick {
                view.findNavController().navigate(R.id.action_loginPassword_to_loginOtp)
            }

//            var phoneNumberValidator =PhoneNumberValidator(requireContext())
//            var xxx = phoneNumberValidator.checkValidPhoneNumber("+91", "2988397522", "IND")
//
//            Log.e("TAG", "xxx "+xxx)


            binding.btSignIn.singleClick {
                if(binding.editTextMobileNumber.text.toString().isEmpty() || binding.editTextMobileNumber.text.toString().length != 10){
                        showSnackBar(getString(R.string.enterMobileNumber))
                    } else if (binding.editTextPassword.text.toString().isEmpty()){
                        showSnackBar(getString(R.string.EnterPassword))
                    } else if(binding.editTextPassword.text.toString().length >= 0 && binding.editTextPassword.text.toString().length < 8){
                        showSnackBar(getString(R.string.InvalidPassword))
                    } else if(!isValidPassword(editTextPassword.text.toString().trim())){
                        showSnackBar(getString(R.string.InvalidPassword))
                    } else {
                        val obj: JSONObject = JSONObject().apply {
                            put("mobile_number", binding.editTextMobileNumber.text.toString())
                            put("password", binding.editTextPassword.text.toString())
                        }
                        viewModel.login(view = requireView(), obj)
                    }
                }
            }


            binding.editTextForgot.singleClick {
                view.findNavController().navigate(R.id.action_loginPassword_to_forgetPassword)
            }

        }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}