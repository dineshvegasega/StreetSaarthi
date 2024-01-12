package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.QuickRegistration1Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.focus
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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuickRegistration1Binding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        OtpTimer.sendOtpTimerData = this

        binding.apply {
            editTextVeryfyOtp.setEnabled(false)

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
                  }
            })


            editTextSendOtp.setOnClickListener {
                if (editTextMobileNumber.text.toString().isEmpty() || editTextMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.enterMobileNumber))
                } else{
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
            }
        }

    }


    @OptIn(DelicateCoroutinesApi::class)
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

    override fun onDestroyView() {
        OtpTimer.sendOtpTimerData = null
        OtpTimer.stopTimer()
        _binding = null
        super.onDestroyView()
    }

}


