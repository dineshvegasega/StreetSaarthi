package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.createNew

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemComplaintType
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateNewVM @Inject constructor(private val repository: Repository): ViewModel() {

    var type : String = "complaint"


    var uploadMediaImage : String ?= null


    var itemComplaintType : ArrayList<ItemComplaintType> = ArrayList()
    var complaintTypeId : Int = 0
    fun complaintType(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemComplaintType>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.complaintType()

                override fun success(response: Response<BaseResponseDC<List<ItemComplaintType>>>) {
                    if (response.isSuccessful){
                        itemComplaintType = response.body()?.data as ArrayList<ItemComplaintType>
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





    fun newFeedback(
        view: View,
        hashMap: RequestBody
    ) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.newFeedback( hashMap)

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful){
                        if (type == "complaint"){
                            showSnackBar(view.resources.getString(R.string.complaint_added_successfully))
                        } else if (type == "feedback"){
                            showSnackBar(view.resources.getString(R.string.feedback_added_successfully))
                        }
                        view.findNavController().navigate(R.id.action_createNew_to_history)
                    } else{
                        showSnackBar(response.body()?.message.orEmpty())
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