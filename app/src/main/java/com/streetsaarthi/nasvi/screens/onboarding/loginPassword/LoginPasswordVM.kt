package com.streetsaarthi.nasvi.screens.onboarding.loginPassword


import android.R.string
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.AUTH
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveData
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveObject
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import com.streetsaarthi.nasvi.utils.getToken
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginPasswordVM @Inject constructor(private val repository: Repository
): ViewModel() {

    fun login(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.login(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        if(response.body()!!.data != null){
                            val _id = Gson().fromJson(response.body()!!.data, Login::class.java).id
                            getToken() {
                                val obj: JSONObject = JSONObject()
                                obj.put("user_id", ""+_id)
                                obj.put("mobile_token", ""+this)
                                token(obj)
                            }
                            profile(view, ""+_id)
                            showSnackBar(view.resources.getString(R.string.logged_in_successfully))
                        }else if(response.body()!!.message == "User does not exist"){
                            showSnackBar(view.resources.getString(R.string.user_does_not_exist))
                        } else {
                            showSnackBar(view.resources.getString(R.string.please_provide_valid_password))
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



    fun token(jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApiWithoutLoader(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.mobileToken(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                    }
                }
                override fun error(message: String) {
//                    super.error(message)
                }
                override fun loading() {
//                    super.loading()
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
                        if(response.body()!!.data != null){
                            saveData(AUTH, response.body()!!.token ?: "")
                            val data = Gson().fromJson(response.body()!!.data, Login::class.java)
                            saveObject(LOGIN_DATA, data)
                            val last = if(data.language == null){
                                "en"
                            }else if(data.language.contains("/")){
                                data.language.substring(data.language.lastIndexOf('/') + 1).replace("'", "")
                            } else {
                                data.language
                            }
                            MainActivity.mainActivity.get()?.reloadActivity(last, Main)
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