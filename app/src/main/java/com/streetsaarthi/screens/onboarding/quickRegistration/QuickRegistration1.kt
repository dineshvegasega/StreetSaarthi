package com.streetsaarthi.screens.onboarding.quickRegistration

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.demo.networking.USER_TYPE
import com.streetsaarthi.R
import com.streetsaarthi.databinding.QuickRegistration1Binding
import com.streetsaarthi.screens.interfaces.CallBackListener
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class QuickRegistration1 : Fragment(), CallBackListener {
    private var _binding: QuickRegistration1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: QuickRegistrationVM by activityViewModels()

    companion object{
        var callBackListener: CallBackListener? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuickRegistration1Binding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        binding.editTextVeryfyOtp.setEnabled(false)

        binding.apply {
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
                  }
            })


            editTextSendOtp.setOnClickListener {
                if (editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterMobileNumber))
                }else{
                    val obj: JSONObject = JSONObject()
                    obj.put("mobile_no", binding.editTextMobileNumber.text.toString())
                    obj.put("slug", "signup")
                    obj.put("user_type", USER_TYPE)
                    viewModel.sendOTP(view = requireView(), obj)
                }
            }

            binding.editTextVeryfyOtp.setOnClickListener {
                if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                }else{
                    val obj: JSONObject = JSONObject()
                    obj.put("mobile_no", binding.editTextMobileNumber.text.toString())
                    obj.put("otp", binding.editTextOtp.text.toString())
                    obj.put("slug", "signup")
                    obj.put("user_type", USER_TYPE)
                    viewModel.verifyOTP(view = requireView(), obj)
                }
            }
        }


    }

    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackA "+pos)
        binding.apply {
            if( pos == 1){
                if(editTextFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.first_name))
                } else if (editTextLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.last_name))
                } else if (editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.mobileNumber))
                } else if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                } else if (viewModel.isOtpVerified == false){
                    showSnackBar(getString(R.string.OTPnotverified))
                } else {
                    viewModel.data.vendor_first_name = editTextFN.text.toString()
                    viewModel.data.vendor_last_name = editTextLN.text.toString()
                    viewModel.data.mobile_no = editTextMobileNumber.text.toString()
                    viewModel.data.otp = editTextOtp.text.toString()

                    Log.e("TAG", "viewModel.dataA "+viewModel.data.toString())

                    QuickRegistration.callBackListener!!.onCallBack(2)
                }
            }
        }

    }


}