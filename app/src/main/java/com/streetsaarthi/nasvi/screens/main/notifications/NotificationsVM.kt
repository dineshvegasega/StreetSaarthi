package com.streetsaarthi.nasvi.screens.main.notifications

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotificationsVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { NotificationsAdapter(this) }


    private var itemNotificationsResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemNotifications : LiveData<BaseResponseDC<Any>> get() = itemNotificationsResult
    fun notifications(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.notifications(jsonObject.getInt("page") , jsonObject.getBoolean("is_read") , jsonObject.getString("user_id") )
                    apiInterface.notifications(jsonObject.getInt("page"), jsonObject.getString("user_id"))

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemNotificationsResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemNotificationsResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemNotificationsSecond : LiveData<BaseResponseDC<Any>> get() = itemNotificationsResultSecond
    fun notificationsSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.notifications(
                        jsonObject.getInt("page") ,
//                        jsonObject.getBoolean("is_read") ,
                        jsonObject.getString("user_id") )
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemNotificationsResultSecond.value =  response.body() as BaseResponseDC<Any>
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





    var deleteNotifications = MutableLiveData<Boolean>(false)
    fun deleteNotification(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.deleteNotification(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        deleteNotifications.value = true
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

}