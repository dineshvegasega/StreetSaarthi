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
import com.streetsaarthi.R
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.login.Login
import com.streetsaarthi.models.login.LoginResponse
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



    fun login(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Login>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.login(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Login>>) {
                    if (response.isSuccessful){
//                                showSnackBar(response.body()?.message.orEmpty())
//                                view.navigateBack()

                        response.body()?.data?.apply {
                            token = response.body()?.token!!
                        }

                        val json = Gson().toJson(response.body())
                        Log.e("TAG", "aaaaaaa "+json)

//                        var ss = response.body()?.data.toString()
                        //  Log.e("TAG", "aaaaaaa "+ss.data?.birth_address)
//                        view.findNavController().navigate(R.id.action_loginPassword_to_webPage, Bundle().apply {
//                            putString("data", ""+json)
//                        })
                        //val json = Gson().toJson(ss)
//                        val gson = Gson()
                        // val json = gson.toJson(ss)
//                        val topic = Gson().fromJson(Gson().toJson(ss), Data::class.java)
//                        Log.e("TAG", "aaaaaaa "+topic.toString())

//                        var sss = <Data> response.body()?.data
//                     sss.birth_address

//                        val list: Data = response.body()?.data

//                        var gson = Gson()
//                        var mMineUserEntity = gson?.fromJson(response.body()?.data.toString(), Data::class.java)



                        view.findNavController().navigate(R.id.action_loginPassword_to_home)


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