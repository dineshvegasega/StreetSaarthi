package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.historyDetail

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.home.HistoryDetailAdapter
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.chat.ItemChat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryDetailVM @Inject constructor(private val repository: Repository): ViewModel() {
    val adapter by lazy { HistoryDetailAdapter() }

    var uploadMediaImage : String ?= null



    private var feedbackConversationLiveData = MutableLiveData<ItemChat>()
    val feedbackConversationLive : LiveData<ItemChat> get() = feedbackConversationLiveData
    fun feedbackConversationDetails(view: View, _id: String, page: String) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<ItemChat>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.feedbackConversationDetails(_id, page)
                override fun success(response: Response<ItemChat>) {
                    if (response.isSuccessful){
                        if(response.body()!!.data != null){
                            feedbackConversationLiveData.value = response.body()
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





    private var feedbackConversationLiveDataSecond = MutableLiveData<ItemChat>()
    val feedbackConversationLiveSecond : LiveData<ItemChat> get() = feedbackConversationLiveDataSecond
    fun feedbackConversationDetailsSecond(view: View, _id: String, page: String) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<ItemChat>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.feedbackConversationDetails(_id, page)
                override fun success(response: Response<ItemChat>) {
                    if (response.isSuccessful){
                        if(response.body()!!.data != null){
                            feedbackConversationLiveDataSecond.value = response.body()
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






    private var addFeedbackConversationLiveData = MutableLiveData<BaseResponseDC<Any>>()
    val addFeedbackConversationLive : LiveData<BaseResponseDC<Any>> get() = addFeedbackConversationLiveData
    fun addFeedbackConversationDetails(view: View, hashMap: RequestBody) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.addFeedbackConversation(hashMap)
                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful){
                        addFeedbackConversationLiveData.value = response.body()
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