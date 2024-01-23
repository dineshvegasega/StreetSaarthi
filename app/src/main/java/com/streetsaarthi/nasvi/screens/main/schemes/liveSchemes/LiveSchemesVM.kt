package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveSchemeBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemSchemeDetail
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.main.webPage.WebPage
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
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
class LiveSchemesVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { LiveSchemesAdapter(this) }



    private var itemLiveSchemesResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveSchemes : LiveData<BaseResponseDC<Any>> get() = itemLiveSchemesResult
    fun liveScheme(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveSchemesResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemLiveSchemesResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveSchemesSecond : LiveData<BaseResponseDC<Any>> get() = itemLiveSchemesResultSecond
    fun liveSchemeSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveSchemesResultSecond.value =  response.body() as BaseResponseDC<Any>
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




    var applyLink = MutableLiveData<Int>(-1)
    fun applyLink(jsonObject: JSONObject, position: Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.applyLink(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        applyLink.value =  position
                    } else {
                        applyLink.value =  -1
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
                    apiInterface.schemeDetail(id = _id)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        var data = Gson().fromJson(response.body()!!.data, ItemSchemeDetail::class.java)

                        when(status){
                            in 1..2 -> {
                                val dialogBinding = DialogBottomLiveSchemeBinding.inflate(root.context.getSystemService(
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
                                        .load(data.scheme_image?.url)
                                        .apply(myOptionsGlide)
                                        .into(ivMap)
                                    textTitle.setText(data.name)
                                    textDesc.setText(data.description)
                                    textHeaderTxt4.setText(data.status)

                                    data.start_at?.let {
                                        textStartDate.text = "${data.start_at.changeDateFormat("yyyy-MM-dd", "dd-MMM-yyyy")}"
                                    }

                                    data.end_at?.let {
                                        textEndDate.text = "${data.end_at.changeDateFormat("yyyy-MM-dd", "dd-MMM-yyyy")}"
                                    }


                                    if (status == 1){
                                        btApply.setText(view.resources.getString(R.string.view))
                                        btApply.visibility = View.GONE
                                    }else{
                                        btApply.setText(view.resources.getString(R.string.apply))
                                        btApply.visibility = View.VISIBLE
                                    }

                                    btApply.setOnClickListener {
                                        if (status == 1){
                                            Handler(Looper.getMainLooper()).post(Thread {
                                                MainActivity.activity.get()?.runOnUiThread {
                                                    data.apply_link?.let {
                                                        val webIntent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse(data.apply_link)
                                                        )
                                                        try {
                                                            root.context.startActivity(webIntent)
                                                        } catch (ex: ActivityNotFoundException) {
                                                        }
                                                    }
                                                }
                                            })
                                        } else {
                                            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                                                if (loginUser != null) {
                                                    val obj: JSONObject = JSONObject().apply {
                                                        put("scheme_id", data?.scheme_id)
                                                        put("user_type", USER_TYPE)
                                                        put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                                                    }
                                                    applyLink(obj, position)
                                                }
                                            }
                                        }
                                        dialog.dismiss()
                                    }

                                    btClose.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                }
                            } else -> {
                                Handler(Looper.getMainLooper()).post(Thread {
                                       MainActivity.activity.get()?.runOnUiThread {
                                           data.apply_link?.let {
                                               val webIntent = Intent(
                                                   Intent.ACTION_VIEW,Uri.parse(data.apply_link))
                                               try {
                                                   root.context.startActivity(webIntent)
                                               } catch (ex: ActivityNotFoundException) {
                                               }
                                           }
                                       }
                                  })
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



