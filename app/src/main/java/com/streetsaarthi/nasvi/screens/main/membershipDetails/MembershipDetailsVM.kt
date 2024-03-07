package com.streetsaarthi.nasvi.screens.main.membershipDetails

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.databinding.LoaderBinding
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.ItemMarketplace
import com.streetsaarthi.nasvi.models.ItemVending
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.IS_LANGUAGE
import com.streetsaarthi.nasvi.screens.onboarding.networking.NETWORK_DIALOG_SHOW
import com.streetsaarthi.nasvi.utils.mainThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MembershipDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {


    var scale10 : Float = 0f
    init {
        scale10 = MainActivity.scale10.toFloat()
    }



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


    var counterNetwork = MutableLiveData<Boolean>(false)
    var itemVending : ArrayList<ItemVending> = ArrayList()
    var vendingTrue = MutableLiveData<Boolean>(false)
    fun vending(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemVending>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.vending()

                override fun success(response: Response<BaseResponseDC<List<ItemVending>>>) {
                    if (response.isSuccessful){
                        if (IS_LANGUAGE){
                            if (MainActivity.context.get()!!
                                    .getString(R.string.englishVal) == "" + locale
                            ) {
                                itemVending = response.body()?.data as ArrayList<ItemVending>
                                vendingTrue.value = true
                            } else {
                                val itemStateTemp = response.body()?.data as ArrayList<ItemVending>
                                show()
                                mainThread {
                                    itemStateTemp.forEach {
                                        delay(50)
                                        val nameChanged: String = callApiTranslate(""+locale, it.name)
                                        apply {
                                            it.name = nameChanged
                                        }
                                    }
                                    itemVending = itemStateTemp
                                    vendingTrue.value = true
                                    hide()
                                }
                            }
                        } else {
                            itemVending = response.body()?.data as ArrayList<ItemVending>
                            vendingTrue.value = true
                        }
                    }
                }

                override fun error(message: String) {
//                    super.error(message)
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



    var itemMarketplace : ArrayList<ItemMarketplace> = ArrayList()
    var marketPlaceTrue = MutableLiveData<Boolean>(false)
    fun marketplace(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemMarketplace>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.marketplace()

                override fun success(response: Response<BaseResponseDC<List<ItemMarketplace>>>) {
                    if (response.isSuccessful){
                        if (IS_LANGUAGE){
                            if (MainActivity.context.get()!!
                                    .getString(R.string.englishVal) == "" + locale
                            ) {
                                itemMarketplace = response.body()?.data as ArrayList<ItemMarketplace>
                                marketPlaceTrue.value = true
                            } else {
                                val itemStateTemp = response.body()?.data as ArrayList<ItemMarketplace>
                                show()
                                mainThread {
                                    itemStateTemp.forEach {
                                        delay(50)
                                        val nameChanged: String = callApiTranslate(""+locale, it.name)
                                        apply {
                                            it.name = nameChanged
                                        }
                                    }
                                    itemMarketplace = itemStateTemp
                                    marketPlaceTrue.value = true
                                    hide()
                                }
                            }
                        } else {
                            itemMarketplace = response.body()?.data as ArrayList<ItemMarketplace>
                            marketPlaceTrue.value = true
                        }
                    }
                }

                override fun error(message: String) {
//                    super.error(message)
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



    fun callApiTranslate(_lang : String, _words: String) : String{
        return repository.callApiTranslate(_lang, _words)
    }
}