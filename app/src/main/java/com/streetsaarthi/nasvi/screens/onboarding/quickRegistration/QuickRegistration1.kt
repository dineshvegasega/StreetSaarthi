package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration


//import com.stfalcon.smsverifycatcher.OnSmsCatchListener
//import com.stfalcon.smsverifycatcher.SmsVerifyCatcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.QuickRegistration1Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject


@AndroidEntryPoint
class QuickRegistration1 : Fragment(), CallBackListener , OtpTimer.SendOtpTimerData {
    private var _binding: QuickRegistration1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: QuickRegistrationVM by activityViewModels()

    companion object{
        var callBackListener: CallBackListener? = null
    }

//    private var smsVerifyCatcher: SmsVerifyCatcher? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuickRegistration1Binding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)

        binding.model = viewModel
        binding.lifecycleOwner = this

        callBackListener = this
        OtpTimer.sendOtpTimerData = this



//        viewModel.addTime.value = "bj"
//        binding.tvTime.visibility = View.VISIBLE


//        viewModel.addTime.observe(viewLifecycleOwner, Observer {
//            binding.tvTime.text = it
//        })
        binding.apply {
//            editTextOtp.setOnTouchListener(OnTouchListener { v, event ->
//                if (MotionEvent.ACTION_HOVER_ENTER == event.action) {
//                   // editTextOtp.setText("" + mQ1)
//                }
//                true // return is important...
//            })

//            editTextOtp.setOnTouchListener(OnTouchListener { v, event ->
//                Log.i("click", "onMtouch")
//                editTextOtp.setSelection(editTextOtp.text!!.length)
//                editTextOtp.setFocusable(true);
//                editTextOtp.requestFocus();
//                false
//            })


            editTextVeryfyOtp.setEnabled(false)

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
                if (it) isTimer = ""
                if (it == true){
                    viewModel.isSendMutable
                    tvTime.visibility = View.GONE
//                    OtpTimer.sendOtpTimerData = null
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
                }
            })


//            smsVerifyCatcher = SmsVerifyCatcher(requireActivity(),
//                OnSmsCatchListener<String?> { message ->
//                    if(message != null && message.length >= 6){
//                        var otp = message.trim().substring(0,6).toInt()
//                            editTextOtp.setText("${otp}")
//                            var start2=editTextOtp.getSelectionStart()
//                            var end2=editTextOtp.getSelectionEnd()
//                            editTextOtp.setSelection(start2,end2)
//                    }
//                })

            editTextSendOtp.setOnClickListener {
//                OtpTimer.startTimer()
                //smsVerifyCatcher!!.onStart()
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                } else{
//                    isFree = true
//                    callMediaPermissions()
                    val obj: JSONObject = JSONObject().apply {
                        put("mobile_no", binding.editTextMobileNumber.text.toString())
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


            editTextMobileNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isTimer.isNotEmpty()) {
                        editTextSendOtp.setEnabled(true)
                        editTextSendOtp.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ResourcesCompat.getColor(
                                    getResources(), R.color._E79D46, null)))
                        QuickRegistration.callBackListener!!.onCallBack(21)
                        editTextMobileNumber.requestFocus()
                    }
                }
            })

            editTextOtp.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isTimer.isNotEmpty()) {
                        editTextSendOtp.setEnabled(true)
                        editTextSendOtp.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ResourcesCompat.getColor(
                                    getResources(), R.color._E79D46, null)))
                        QuickRegistration.callBackListener!!.onCallBack(21)
                        editTextOtp.requestFocus()
                    }
                }
            })

        }
    }





    private fun callMediaPermissions() {
        activityResultLauncher.launch(
                arrayOf()
            )
    }



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
                        val obj: JSONObject = JSONObject().apply {
                            put("mobile_no", binding.editTextMobileNumber.text.toString())
                            put("slug", "signup")
                            put("user_type", USER_TYPE)
                        }
                        viewModel.sendOTP(view = requireView(), obj)
//                        smsVerifyCatcher!!.onStart()

                } else {
                    // Permission is denied
                    Log.e("TAG", "222222"+permissionName)
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
                } else if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
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
//            } else if (pos == 111){
//                viewModel.isSend.value = false
//                viewModel.isSendMutable.value = false
            }
        }

    }


    var isTimer = ""
    @OptIn(DelicateCoroutinesApi::class)
    override fun otpData(string: String) {
        Log.e("TAG", "otpData "+string)
        isTimer = string
        binding.apply {
            tvTime.visibility = if (string.isNotEmpty()) View.VISIBLE else View.GONE
            tvTime.text = getString(R.string.the_verify_code_will_expire_in_00_59, string)

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


