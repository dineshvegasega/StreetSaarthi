package com.streetsaarthi.nasvi.screens.main.notices.liveNotices

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.networking.ApiInterface
import com.streetsaarthi.nasvi.networking.CallHandler
import com.streetsaarthi.nasvi.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveNoticeBinding
import com.streetsaarthi.nasvi.databinding.LoaderBinding
import com.streetsaarthi.nasvi.models.BaseResponseDC
import com.streetsaarthi.nasvi.models.ItemLiveNotice
import com.streetsaarthi.nasvi.models.ItemNoticeDetail
import com.streetsaarthi.nasvi.networking.NETWORK_DIALOG_SHOW
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.glideImage
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LiveNoticesVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { LiveNoticesAdapter(this) }

    var counterNetwork = MutableLiveData<Boolean>(false)



    var locale: Locale = Locale.getDefault()
    var alertDialog: AlertDialog? = null
    init {
        val alert = AlertDialog.Builder(MainActivity.activity.get())
        val binding =
            LoaderBinding.inflate(LayoutInflater.from(MainActivity.activity.get()), null, false)
        alert.setView(binding.root)
        alert.setCancelable(false)
        alertDialog = alert.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
                alertDialog?.show()
            }
        }
    }

    fun hide() {
        viewModelScope.launch {
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
        }
    }


    private var itemLiveNoticeResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveNotice : LiveData<BaseResponseDC<Any>> get() = itemLiveNoticeResult
    fun liveNotice(jsonObject: JSONObject) = viewModelScope.launch {
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



    private var itemLiveNoticeResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveNoticeSecond : LiveData<BaseResponseDC<Any>> get() = itemLiveNoticeResultSecond
    fun liveNoticeSecond(jsonObject: JSONObject) = viewModelScope.launch {
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




    fun viewDetail(itemLiveNotice: ItemLiveNotice, position: Int, root: View, status: Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.noticeDetail(id = ""+itemLiveNotice.notice_id)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val data = Gson().fromJson(response.body()!!.data, ItemNoticeDetail::class.java)
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
                                    data.notice_image?.url?.glideImage(root.context, ivMap)
                                    textTitle.setText(itemLiveNotice.name)
                                    textDesc.setText(itemLiveNotice.description)
                                    textHeaderTxt4.setText(data.status)
                                    textHeaderTxt4.visibility = View.GONE

                                    data.end_date?.let {
                                        textEndDate.text = HtmlCompat.fromHtml("${root.context.resources.getString(R.string.end_date, "<b>"+data.end_date.changeDateFormat("yyyy-MM-dd", "dd MMM, yyyy")+"</b>")}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                                    }

                                    btApply.visibility = View.GONE

                                    btClose.singleClick {
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




    fun callApiTranslate(_lang : String, _words: String) : String{
        return repository.callApiTranslate(_lang, _words)
    }
}