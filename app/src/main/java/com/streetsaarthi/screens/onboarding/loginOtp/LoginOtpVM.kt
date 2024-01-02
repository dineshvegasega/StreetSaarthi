package com.streetsaarthi.screens.onboarding.loginOtp

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.datastore.DataStoreKeys
import com.streetsaarthi.datastore.DataStoreUtil
import com.streetsaarthi.R
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.login.Login
import com.streetsaarthi.networking.getJsonRequestBody
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginOtpVM @Inject constructor(private val repository: Repository): ViewModel() {

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
                        isSend.value = true
//                        isSend.value = true
                        showSnackBar(response.body()?.message.orEmpty())
                    } else{
                        isSend.value = false
//                        isSend.value = true
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



    fun verifyOTPData(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.verifyOTPData(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        DataStoreUtil.saveData(DataStoreKeys.AUTH, response.body()!!.token ?: "")
                        DataStoreUtil.saveObject(
                            DataStoreKeys.LOGIN_DATA,
                            Gson().fromJson(response.body()!!.data, Login::class.java)
                        )
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController().navigate(R.id.action_loginOtp_to_dashboard)
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