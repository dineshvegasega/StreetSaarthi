package com.streetsaarthi.nasvi.screens.main.informationCenter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomInformationCenterBinding
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemInformationDetail
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.glideImage
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class InformationCenterVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { InformationCenterAdapter(this) }


    private var itemInformationCenterResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemInformationCenter : LiveData<BaseResponseDC<Any>> get() = itemInformationCenterResult
    fun informationCenter(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.informationCenter(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemInformationCenterResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemInformationCenterResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemInformationCenterSecond : LiveData<BaseResponseDC<Any>> get() = itemInformationCenterResultSecond
    fun informationCenterSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.informationCenter(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemInformationCenterResultSecond.value =  response.body() as BaseResponseDC<Any>
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
                    apiInterface.informationDetail(id = _id)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        var data = Gson().fromJson(response.body()!!.data, ItemInformationDetail::class.java)
                        when(status){
                            in 1..2 -> {
                                val dialogBinding = DialogBottomInformationCenterBinding.inflate(root.context.getSystemService(
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
                                    data.cover_image?.url?.glideImage(root.context, ivMap)
                                    textTitle.setText(data.title)
                                    textDesc.setText(data.description)
//                                    textHeaderTxt4.setText(data.status)
                                    textStartDate.visibility = View.GONE
                                    textEndDate.visibility = View.GONE

//                                    data.training_end_at?.let {
//                                        textEndDate.text = HtmlCompat.fromHtml("${root.context.resources.getString(R.string.end_date, "<b>"+data.training_end_at.changeDateFormat("yyyy-MM-dd", "dd MMM")+"</b>")}", HtmlCompat.FROM_HTML_MODE_LEGACY)
//                                    }

                                    btApply.text = root.context.resources.getString(R.string.watch)

                                    btApply.setOnClickListener {
                                        if (status == 1){
                                            Handler(Looper.getMainLooper()).post(Thread {
                                                MainActivity.activity.get()?.runOnUiThread {
                                                    data.information_link?.let {
                                                        val webIntent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse(data.information_link)
                                                        )
                                                        try {
                                                            root.context.startActivity(webIntent)
                                                        } catch (ex: ActivityNotFoundException) {
                                                        }
                                                    }
                                                }
                                            })
                                        }
                                        dialog.dismiss()
                                    }

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