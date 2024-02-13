package com.streetsaarthi.nasvi.screens.main.changeMobile

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.main.profiles.ProfessionalDetails
import com.streetsaarthi.nasvi.screens.main.profiles.Profiles
import com.streetsaarthi.nasvi.screens.onboarding.quickRegistration.QuickRegistrationVM
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangeMobileVM @Inject constructor(private val repository: Repository): ViewModel() {

    var isAgree = MutableLiveData<Boolean>(false)


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



    fun profileUpdate(view: View, _id: String, hashMap: RequestBody) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profileUpdate(_id, hashMap)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        profile(view, response.body()!!.vendor_id!!)
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



    fun profile(view: View, _id: String) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profile(_id)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        showSnackBar(view.resources.getString(R.string.mobile_number_updated_successfully))
                        if(response.body()!!.data != null){
                            DataStoreUtil.saveData(
                                DataStoreKeys.AUTH,
                                response.body()!!.token ?: ""
                            )
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LOGIN_DATA,
                                Gson().fromJson(response.body()!!.data, Login::class.java)
                            )
                            view.findNavController().navigateUp()
                        }
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