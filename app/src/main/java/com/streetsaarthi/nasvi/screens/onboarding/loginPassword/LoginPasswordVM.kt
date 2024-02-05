package com.streetsaarthi.nasvi.screens.onboarding.loginPassword


import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.AUTH
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveData
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveObject
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
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
                            profile(view, ""+Gson().fromJson(response.body()!!.data, Login::class.java).id)
                            showSnackBar(view.resources.getString(R.string.logged_in_successfully))
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




    fun profile(view: View, _id: String) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profile(_id)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
//                        showSnackBar(response.body()?.message.orEmpty())
                        if(response.body()!!.data != null){
                            Log.e("TAG", "aaaaa")
                            saveData(AUTH, response.body()!!.token ?: "")
                            saveObject(LOGIN_DATA, Gson().fromJson(response.body()!!.data, Login::class.java))
                            MainActivity.mainActivity.get()!!.callBack()
                                view.findNavController().navigate(R.id.action_loginPassword_to_dashboard)
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