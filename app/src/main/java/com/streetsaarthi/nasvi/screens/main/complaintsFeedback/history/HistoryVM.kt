package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.history

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.onboarding.networking.NETWORK_DIALOG_SHOW
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryVM @Inject constructor(private val repository: Repository): ViewModel() {
    val adapter by lazy { HistoryAdapter(this) }

    var counterNetwork = MutableLiveData<Boolean>(false)


    private var itemHistoryResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemHistory : LiveData<BaseResponseDC<Any>> get() = itemHistoryResult
    fun history(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.complaintFeedbackHistory(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemHistoryResult.value = response.body() as BaseResponseDC<Any>
                    }
                }

                override fun error(message: String) {
//                    super.error(message)
//                    showSnackBar(message)
                    if(NETWORK_DIALOG_SHOW){
                        counterNetwork.value = true
                    }
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }



    private var itemHistoryResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemHistorySecond : LiveData<BaseResponseDC<Any>> get() = itemHistoryResultSecond
    fun historySecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.complaintFeedbackHistory(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemHistoryResultSecond.value =  response.body() as BaseResponseDC<Any>
                    }
                }

                override fun error(message: String) {
//                    super.error(message)
//                    showSnackBar(message)
                    if(NETWORK_DIALOG_SHOW){
                        counterNetwork.value = true
                    }
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }



}