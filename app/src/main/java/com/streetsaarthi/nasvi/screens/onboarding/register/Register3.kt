package com.streetsaarthi.nasvi.screens.onboarding.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.Register3Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.focus
import com.streetsaarthi.nasvi.utils.isValidPassword
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class Register3  : Fragment() , CallBackListener , OtpTimer.SendOtpTimerData {
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
        OtpTimer.sendOtpTimerData = this

        binding.apply {
            editTextVeryfyOtp.setEnabled(false)

            var counter = 0
            var start: Int
            var end: Int
            imgCreatePassword.setOnClickListener {
                if(counter == 0){
                    counter = 1
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_open)
                    start=editTextCreatePassword.getSelectionStart()
                    end=editTextCreatePassword.getSelectionEnd()
                    editTextCreatePassword.setTransformationMethod(null)
                    editTextCreatePassword.setSelection(start,end)
                }else{
                    counter = 0
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_closed)
                    start=editTextCreatePassword.getSelectionStart()
                    end=editTextCreatePassword.getSelectionEnd()
                    editTextCreatePassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextCreatePassword.setSelection(start,end)
                }
            }


            var counter2 = 0
            var start2: Int
            var end2: Int
            imgReEnterPassword.setOnClickListener {
                if(counter2 == 0){
                    counter2 = 1
                    imgReEnterPassword.setImageResource(R.drawable.ic_eye_open)
                    start2=editTextReEnterPassword.getSelectionStart()
                    end2=editTextReEnterPassword.getSelectionEnd()
                    editTextReEnterPassword.setTransformationMethod(null)
                    editTextReEnterPassword.setSelection(start2,end2)
                }else{
                    counter2 = 0
                    imgReEnterPassword.setImageResource(R.drawable.ic_eye_closed)
                    start2=editTextReEnterPassword.getSelectionStart()
                    end2=editTextReEnterPassword.getSelectionEnd()
                    editTextReEnterPassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextReEnterPassword.setSelection(start2,end2)
                }
            }

            viewModel.isSend.value = false
            viewModel.isSend.observe(viewLifecycleOwner, Observer {
                editTextSendOtp.setText(if (it == true) {getString(R.string.resendOtp)} else {getString(R.string.send_otp)})
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

            viewModel.isSendMutable.value = false
            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    tvTime.visibility = View.GONE
                    OtpTimer.sendOtpTimerData = null
                    OtpTimer.stopTimer()
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
                    getAgreeValue()
                }
            })



            editTextMobileNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    editTextSendOtp.setEnabled(true)
                    editTextSendOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                    viewModel.isOtpVerified = false
                    Register.callBackListener!!.onCallBack(21)
                    editTextMobileNumber.requestFocus()
                }
            })

            editTextOtp.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    editTextSendOtp.setEnabled(true)
                    editTextSendOtp.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                    viewModel.isOtpVerified = false
                    Register.callBackListener!!.onCallBack(21)
                    editTextOtp.requestFocus()
                }
            })


            textTerms.setOnClickListener {
                openTermConditionsDialog()
            }


            editTextSendOtp.setOnClickListener {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                }else{
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", editTextMobileNumber.text.toString())
                        put("slug", "signup")
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
                        put("mobile_no", editTextMobileNumber.text.toString())
                        put("otp", editTextOtp.text.toString())
                        put("slug", "signup")
                        put("user_type", USER_TYPE)
                    }
                    viewModel.verifyOTP(view = requireView(), obj)
                }
            }



            cbRememberMe.setOnClickListener {
                getAgreeValue()
            }




            editTextCreatePassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
//                    editTextCreatePassword.requestFocus()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                    editTextCreatePassword.requestFocus()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.e("TAG", "countAA "+start)
                    if(!editTextCreatePassword.text.toString().isEmpty()){
                        if(start >= 0 && start < 8){
                            textCreatePasswrordMsg.setText(R.string.InvalidPassword)
                            textCreatePasswrordMsg.visibility = View.VISIBLE
                        } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                            textCreatePasswrordMsg.setText(R.string.InvalidPassword)
                            textCreatePasswrordMsg.visibility = View.VISIBLE
                        } else {
                            textCreatePasswrordMsg.visibility = View.GONE
                        }
                    }
                    editTextCreatePassword.requestFocus()
//                    editTextCreatePassword.setCursorVisible(true)
                    getAgreeValue()

//                    editText.postDelayed(Runnable {
//                        val keyboard = catalougeJobDetailFragment
//                            .getActivity().getSystemService(
//                                Context.INPUT_METHOD_SERVICE
//                            ) as InputMethodManager
//                        keyboard.showSoftInput(commentEt, 0)
//                    }, 20)
                }
            })


            editTextReEnterPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!editTextCreatePassword.text.toString().isEmpty()){
                        if(!editTextReEnterPassword.text.toString().isEmpty()){
//                            if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
//                                textReEnterPasswrordMsg.setText(R.string.InvalidPassword)
//                                textReEnterPasswrordMsg.visibility = View.VISIBLE
//                            } else
                            if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                                textReEnterPasswrordMsg.setText(R.string.CreatePasswordReEnterPasswordisnotsame)
                                textReEnterPasswrordMsg.visibility = View.VISIBLE
                                getAgreeValue()
                            } else {
                                textReEnterPasswrordMsg.visibility = View.GONE
                                getAgreeValue()
                            }
                        }
                    }
                    editTextReEnterPassword.requestFocus()
                    getAgreeValue()
                }
            })



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
                } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextReEnterPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.reEnterPassword))
                } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextReEnterPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                    showSnackBar(getString(R.string.CreatePasswordReEnterPasswordisnotsame))
                } else if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
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


    override fun otpData(string: String) {
        binding.apply {
            tvTime.visibility = if (string.isNotEmpty()) View.VISIBLE else View.GONE
            tvTime.text = getString(R.string.the_verify_code_will_expire_in_00_59, string)

            if(MainActivity.isOpen.get() == true){
                editTextOtp.focus()
            }

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


    fun getAgreeValue(){
        binding.apply {
            if(editTextCreatePassword.text.toString().isEmpty()){
                viewModel.isAgree.value = false
            } else if(editTextCreatePassword.text.toString().length >= 0 && editTextCreatePassword.text.toString().length < 8){
                viewModel.isAgree.value = false
            } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                viewModel.isAgree.value = false
            } else if (editTextReEnterPassword.text.toString().isEmpty()){
                viewModel.isAgree.value = false
//            } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
//                viewModel.isAgree.value = false
//            } else if(!isValidPassword(editTextReEnterPassword.text.toString().trim())){
//                viewModel.isAgree.value = false
//            } else if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
//                viewModel.isAgree.value = false
            }  else if (viewModel.isOtpVerified == false){
                viewModel.isAgree.value = false
            } else if (!cbRememberMe.isChecked){
                viewModel.isAgree.value = false
            } else {
                viewModel.isAgree.value = true
            }
        }
    }



    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResumeZZZ")
        Handler(Looper.getMainLooper()).postDelayed({
            getAgreeValue()
        }, 200)
    }

    override fun onDestroyView() {
        viewModel.isAgree.value = false
        OtpTimer.sendOtpTimerData = null
        OtpTimer.stopTimer()
        _binding = null
        super.onDestroyView()
    }
}

//implementation("com.google.firebase:firebase-analytics")