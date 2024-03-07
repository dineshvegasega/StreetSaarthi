package com.streetsaarthi.nasvi.screens.main.settings

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveData
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.saveObject
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<Item> ?= ArrayList()
    var locale: Locale = Locale.getDefault()

    var appLanguage = MutableLiveData<String>("")


    init {
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.english), MainActivity.context.get()!!.getString(R.string.englishVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.bengali), MainActivity.context.get()!!.getString(R.string.bengaliVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.gujarati), MainActivity.context.get()!!.getString(R.string.gujaratiVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.hindi), MainActivity.context.get()!!.getString(R.string.hindiVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.kannada), MainActivity.context.get()!!.getString(R.string.kannadaVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.malayalam), MainActivity.context.get()!!.getString(R.string.malayalamVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.marathi), MainActivity.context.get()!!.getString(R.string.marathiVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.punjabi), MainActivity.context.get()!!.getString(R.string.punjabiVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.tamil), MainActivity.context.get()!!.getString(R.string.tamilVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.telugu), MainActivity.context.get()!!.getString(R.string.teluguVal),false))
        itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.urdu), MainActivity.context.get()!!.getString(R.string.urduVal),false))
        //itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.assamese), MainActivity.context.get()!!.getString(R.string.assameseVal),false))


        for (item in itemMain!!.iterator()) {
            if(item.locale == ""+locale){
                item.apply {
                    item.isSelected = true
                }
                Handler(Looper.getMainLooper()).postDelayed(Thread {
                    appLanguage.value = item.name
                }, 50)
            }
        }
    }

    data class Item (
        var name: String = "",
        var locale: String = "",
        var isSelected: Boolean? = false
    )





    fun notificationUpdate(_id : String,  hashMap: RequestBody, value : Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.saveSettings(hashMap)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        profile(_id, value)
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


    var itemNotificationUpdateResult = MutableLiveData<Boolean>(false)
    fun profile(_id: String, value : Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profile(_id)
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        if(response.body()!!.data != null){
                            saveData(
                                DataStoreKeys.AUTH,
                                response.body()!!.token ?: ""
                            )
                            val data = Gson().fromJson(response.body()!!.data, Login::class.java)
                            saveObject(
                                DataStoreKeys.LOGIN_DATA,
                                data
                            )
                            itemNotificationUpdateResult.value = true

                            if(value == 1){
                                val last = if(data.language == null){
                                    "en"
                                }else if(data.language.contains("/")){
                                    data.language.substring(data.language.lastIndexOf('/') + 1).replace("'", "")
                                } else {
                                    data.language
                                }
                                MainActivity.mainActivity.get()?.reloadActivity(last, Main)
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