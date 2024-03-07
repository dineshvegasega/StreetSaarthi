package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuickRegistrationVM @Inject constructor(private val repository: Repository): ViewModel() {

    var isAgree = MutableLiveData<Boolean>(false)

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



    fun register(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.register(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful){
                        showSnackBar(response.body()?.message.orEmpty())
                            view.findNavController().navigate(R.id.action_quickRegistration_to_registerSuccessful, Bundle().apply {
                                putString("key", jsonObject.getString("vendor_first_name"))
                            })
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