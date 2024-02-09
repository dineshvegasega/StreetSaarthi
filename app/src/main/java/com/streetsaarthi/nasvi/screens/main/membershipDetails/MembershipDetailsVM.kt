package com.streetsaarthi.nasvi.screens.main.membershipDetails

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemMarketplace
import com.streetsaarthi.nasvi.models.mix.ItemVending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MembershipDetailsVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemVending : ArrayList<ItemVending> = ArrayList()
    var vendingId : Int = 0
    var vendingTrue = MutableLiveData<Boolean>(false)
    fun vending(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemVending>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.vending()

                override fun success(response: Response<BaseResponseDC<List<ItemVending>>>) {
                    if (response.isSuccessful){
                        itemVending = response.body()?.data as ArrayList<ItemVending>
                        vendingTrue.value = true
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



    var itemMarketplace : ArrayList<ItemMarketplace> = ArrayList()
    var marketplaceId : Int = 0
    var marketPlaceTrue = MutableLiveData<Boolean>(false)
    fun marketplace(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemMarketplace>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.marketplace()

                override fun success(response: Response<BaseResponseDC<List<ItemMarketplace>>>) {
                    if (response.isSuccessful){
                        itemMarketplace = response.body()?.data as ArrayList<ItemMarketplace>
                        marketPlaceTrue.value = true
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





//
//    private var itemAdsResult = MutableLiveData< ArrayList<ItemAds>>()
//    val itemAds : LiveData<ArrayList<ItemAds>> get() = itemAdsResult
//    fun adsList(view: View) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemAds>>>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.adsList()
//
//                override fun success(response: Response<BaseResponseDC<List<ItemAds>>>) {
//                    if (response.isSuccessful){
//                        itemAdsResult.value = response.body()?.data as ArrayList<ItemAds>
//                    }
//                }
//
//                override fun error(message: String) {
//                    super.error(message)
//                }
//
//                override fun loading() {
//                    super.loading()
//                }
//            }
//        )
//    }

}