package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.OtpTimer
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuickRegistrationVM @Inject constructor(private val repository: Repository): ViewModel() {
    var data : Model = Model()

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
                        } else {
                            isSend.value = false
                        }
                        showSnackBar(response.body()?.message.orEmpty())
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
//                        if(response.body()?.message == view.rootView.context.getString(R.string.InvalidOTP)){
//                            isOtpVerified = false
                           // showSnackBar(response.body()?.message.orEmpty())
//                        } else {
//                            isOtpVerified = true
//                            showSnackBar(response.body()?.message.orEmpty())
//                        }
                        showSnackBar(response.body()?.message.orEmpty())
                        if(response.body()?.data != null){
                            isOtpVerified = true
                            isSendMutable.value = true
                        } else {
                            isOtpVerified = false
                            isSendMutable.value = false
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



    fun register(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.register(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController().navigate(R.id.action_quickRegistration_to_registerSuccessful)
                    } else{
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


    fun registerWithFiles(
        view: View,
        jsonObject: JSONObject,
        hashMap: RequestBody
    ) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.registerWithFiles( hashMap)

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController().navigate(R.id.action_register_to_registerSuccessful)
                    } else{
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


    data class Model(
        var vendor_first_name : String ?= null,
        var vendor_last_name : String ?= null,
        var mobile_no : String ?= null,
        var otp : String ?= null,
        var password : String ?= null
        )
}