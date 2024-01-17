package com.streetsaarthi.nasvi.screens.onboarding.forgetPassword

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.navigateBack
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordVM @Inject constructor(private val repository: Repository): ViewModel() {
    var isSend = MutableLiveData<Boolean>(false)
    var isSendMutable = MutableLiveData<Boolean>(false)

    var isOtpVerified = false

    fun sendOTP(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.sendOTP(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        if(response.body()?.message == "OTP Sent successfully"){
                            isSend.value = true
                            var number = jsonObject.getString("mobile_no")
                            showSnackBar(view.resources.getString(R.string.otp_sent, number))
                        } else {
                            isSend.value = false
                            showSnackBar(view.resources.getString(R.string.user_already_exist))
                        }
                    } else{
                        isSend.value = false
                        showSnackBar(response.body()?.message.orEmpty())
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    showSnackBar(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }



    fun verifyOTP(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.verifyOTP(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        if(response.body()?.data != null){
                            isOtpVerified = true
                            isSendMutable.value = true
                            showSnackBar(view.resources.getString(R.string.otp_Verified_successfully))
                        } else {
                            isOtpVerified = false
                            isSendMutable.value = false
                            showSnackBar(view.resources.getString(R.string.invalid_OTP))
                        }
                    } else{
                        isOtpVerified = false
                        isSendMutable.value = false
                        showSnackBar(response.body()?.message.orEmpty())
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    showSnackBar(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }



    fun passwordupdate(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.passwordupdate(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful){
                                showSnackBar(response.body()?.message.orEmpty())
                                view.navigateBack()
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }
}