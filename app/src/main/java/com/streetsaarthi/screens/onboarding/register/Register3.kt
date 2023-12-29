package com.streetsaarthi.screens.onboarding.register

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.demo.networking.USER_TYPE
import com.google.android.material.button.MaterialButton
import com.streetsaarthi.R
import com.streetsaarthi.databinding.Register3Binding
import com.streetsaarthi.screens.interfaces.CallBackListener
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class Register3  : Fragment() , CallBackListener {
    private var _binding: Register3Binding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()

    companion object{
        var callBackListener: CallBackListener? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Register3Binding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this

        binding.editTextVeryfyOtp.setEnabled(false)
        binding.apply {
            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                binding.editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(
                    R.string.send_otp)})
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


            textTerms.setOnClickListener {
                openTermConditionsDialog()
            }


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

    private fun openTermConditionsDialog() {
        val mybuilder= Dialog(requireActivity())
        mybuilder.setContentView(R.layout.dialog_termsconditions)
        mybuilder.show()
        val window=mybuilder.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window!!.setBackgroundDrawableResource(R.color._00000000)

        val yes = mybuilder.findViewById<AppCompatImageView>(R.id.imageCross)
        yes?.setOnClickListener {
            mybuilder.dismiss()
        }
    }


    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackC " + pos)
        binding.apply {
            if (pos == 5) {
                if(editTextCreatePassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.createPassword))
                } else if(editTextCreatePassword.text.toString().length >= 0 && editTextCreatePassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextReEnterPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.reEnterPassword))
                } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                    showSnackBar(getString(R.string.CreatePasswordReEnterPasswordisnotsame))
                } else if (editTextMobileNumber.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterMobileNumber))
                } else if (editTextOtp.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.enterOtp))
                } else if (viewModel.isOtpVerified == false){
                    showSnackBar(getString(R.string.OTPnotverified))
                }  else if (!cbRememberMe.isChecked){
                    showSnackBar(getString(R.string.Pleaseselectagree))
                } else {
                    viewModel.data.password = editTextCreatePassword.text.toString()
                    viewModel.data.mobile_no = editTextMobileNumber.text.toString()
                    viewModel.data.otp = editTextOtp.text.toString()
                    Log.e("TAG", "viewModel.dataC "+viewModel.data.toString())
                    Register.callBackListener!!.onCallBack(6)
                }
            }
        }
    }
}