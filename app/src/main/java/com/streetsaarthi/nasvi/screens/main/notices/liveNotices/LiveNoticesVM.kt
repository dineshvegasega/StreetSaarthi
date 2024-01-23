package com.streetsaarthi.nasvi.screens.main.notices.liveNotices

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.BR
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveNoticeBinding
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveTrainingBinding
import com.streetsaarthi.nasvi.databinding.ItemLiveNoticeBinding
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemLiveNotice
import com.streetsaarthi.nasvi.models.mix.ItemNoticeDetail
import com.streetsaarthi.nasvi.models.mix.ItemTrainingDetail
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.interfaces.PaginationAdapterCallback
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemesVM
import com.streetsaarthi.nasvi.screens.main.training.liveTraining.LiveTrainingVM
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.GlideApp
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.myOptionsGlide
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LiveNoticesVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { LiveNoticesAdapter(this) }



    private var itemLiveNoticeResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveNotice : LiveData<BaseResponseDC<Any>> get() = itemLiveNoticeResult
    fun liveNotice(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveNotice(requestBody = jsonObject.getJsonRequestBody())
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
                    apiInterface.liveNotice(requestBody = jsonObject.getJsonRequestBody())
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




    fun viewDetail(_id: String, position: Int, root: View, status : Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.noticeDetail(id = _id)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        var data = Gson().fromJson(response.body()!!.data, ItemNoticeDetail::class.java)
                        when(status){
                            in 1..2 -> {
                                val dialogBinding = DialogBottomLiveNoticeBinding.inflate(root.context.getSystemService(
                                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                )
                                val dialog = BottomSheetDialog(root.context)
                                dialog.setContentView(dialogBinding.root)
                                dialog.setOnShowListener { dia ->
                                    val bottomSheetDialog = dia as BottomSheetDialog
                                    val bottomSheetInternal: FrameLayout =
                                        bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                                    bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                                }
                                dialog.show()

                                dialogBinding.apply {
                                    GlideApp.with(root.context)
                                        .load(data.notice_image?.url)
                                        .apply(myOptionsGlide)
                                        .into(ivMap)
                                    textTitle.setText(data.name)
                                    textDesc.setText(data.description)
                                    textHeaderTxt4.setText(data.status)

                                    data.end_date?.let {
                                        textEndDate.text = "${data.end_date.changeDateFormat("yyyy-MM-dd", "dd-MMM-yyyy")}"
                                    }

                                    btApply.visibility = View.GONE

                                    btClose.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                }
                            }

                        }
                    } else {

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