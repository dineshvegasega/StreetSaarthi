package com.streetsaarthi.screens.onboarding.loginPassword

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.models.Items
import com.streetsaarthi.models.login.LoginResponse
import com.streetsaarthi.networking.getJsonRequestBody
import com.google.gson.Gson
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
    val readResult : LiveData<Items> get() = result


    fun login(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Login>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.login(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Login>>) {
                    if (response.isSuccessful){

//                        preferences.storeKey(TOKEN, response.body()!!.data!!.token)
//                        dataStore.saveObject(LOGIN_DATA, response.body()!!.data)

//                                showSnackBar(response.body()?.message.orEmpty())
//                                view.navigateBack()

//                        response.body()?.data?.apply {
//                            token = response.body()?.token!!
//                        }

//                        val json = Gson().toJson(response.body())
//                        Log.e("TAG", "aaaaaaa "+json)

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