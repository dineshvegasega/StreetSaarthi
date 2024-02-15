package com.streetsaarthi.nasvi.screens.main.profiles.nomineeDetails

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemNomineeData
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NomineeDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {
    var isEditable = MutableLiveData<Boolean>(false)

    var relationType1 = ""
    var relationName1 = ""

    var relationType2 = ""
    var relationName2 = ""

    var relationType3 = ""
    var relationName3 = ""

    var relationType4 = ""
    var relationName4 = ""

    var relationType5 = ""
    var relationName5 = ""





    var updateNominee = MutableLiveData<Boolean>(false)
    fun  updateNomineeDetails(view: View, _id: String, toString: HashMap<String, String>) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.updateNomineeDetails(_id, toString)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        updateNominee.value = true
                        showSnackBar(view.resources.getString(R.string.nominee_added_updated_successfully))
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




    var mutableLiveData = MutableLiveData<Boolean>(false)
    val pairList = ArrayList<Pair<String, String>>()
    fun nomineeDetails(view: View, requestBody: RequestBody) = viewModelScope.launch {
        pairList.clear()
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.nomineeDetails(requestBody = requestBody)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val typeToken = object : TypeToken<ItemNomineeData>() {}.type
                        if(Gson().toJson(response.body()!!.data) != "[]"){
                            val changeValue = Gson().fromJson<ItemNomineeData>(Gson().toJson(response.body()!!.data), typeToken)
                                changeValue.nominee.forEach {
                                val product: HashMap<String, String> ?= it
                                var count = 0
                                product?.values?.forEach {
                                    pairList.add(Pair(""+product?.keys?.elementAt(count), ""+it))
                                    count ++
                                }
                                mutableLiveData.value = true
                            }
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






