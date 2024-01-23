package com.streetsaarthi.nasvi.screens.main.notices.allNotices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.databinding.ItemAllNoticesBinding
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AllNoticesVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemAllNoticesBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemAllNoticesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemAllNoticesBinding, dataClass: String, position: Int) {

        }
    }





    private var itemLiveNoticeResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveNotice : LiveData<BaseResponseDC<Any>> get() = itemLiveNoticeResult
    fun liveNotice(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.allNoticeList(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveNoticeResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemLiveNoticeResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveNoticeSecond : LiveData<BaseResponseDC<Any>> get() = itemLiveNoticeResultSecond
    fun liveNoticeSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.allNoticeList(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveNoticeResultSecond.value =  response.body() as BaseResponseDC<Any>
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