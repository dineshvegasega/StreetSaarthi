package com.streetsaarthi.nasvi.screens.main.training.allTraining

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
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.nasvi.databinding.ItemAllTrainingBinding
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AllTrainingVM @Inject constructor(private val repository: Repository): ViewModel() {
    var itemMain : ArrayList<String> ?= ArrayList()
    init {
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
        itemMain?.add("")
    }


    val photosAdapter = object : GenericAdapter<ItemAllTrainingBinding, String>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemAllTrainingBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemAllTrainingBinding, dataClass: String, position: Int) {

        }
    }





    private var itemLiveTrainingResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveTraining : LiveData<BaseResponseDC<Any>> get() = itemLiveTrainingResult
    fun liveTraining(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.allTrainingList(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveTrainingResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemLiveTrainingResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveTrainingSecond : LiveData<BaseResponseDC<Any>> get() = itemLiveTrainingResultSecond
    fun liveTrainingSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.allTrainingList(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveTrainingResultSecond.value =  response.body() as BaseResponseDC<Any>
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