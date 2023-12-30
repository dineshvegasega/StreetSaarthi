package com.streetsaarthi.screens.onboarding.loginPassword

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.models.Items
import com.streetsaarthi.networking.getJsonRequestBody
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.datastore.DataStoreKeys.AUTH
import com.streetsaarthi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.datastore.DataStoreUtil.saveData
import com.streetsaarthi.datastore.DataStoreUtil.saveObject
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.login.Login

import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginPasswordVM @Inject constructor(private val repository: Repository
): ViewModel() {

    private var result = MutableLiveData<Items>()
//    val readResult : LiveData<Items> get() = result


    fun login(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.login(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        saveData(AUTH, response.body()!!.token ?: "")
                        saveObject(LOGIN_DATA, Gson().fromJson(response.body()!!.data, Login::class.java))
                        showSnackBar(response.body()?.message.orEmpty())
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