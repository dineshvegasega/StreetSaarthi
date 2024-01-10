package com.streetsaarthi.screens.main.profiles

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.mix.ItemDistrict
import com.streetsaarthi.models.mix.ItemMarketplace
import com.streetsaarthi.models.mix.ItemOrganization
import com.streetsaarthi.models.mix.ItemPanchayat
import com.streetsaarthi.models.mix.ItemPincode
import com.streetsaarthi.models.mix.ItemState
import com.streetsaarthi.models.mix.ItemVending
import com.streetsaarthi.networking.getJsonRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfilesVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemState : ArrayList<ItemState> = ArrayList()
    var stateId : Int = 0

    var itemDistrict : ArrayList<ItemDistrict> = ArrayList()
    var districtId : Int = 0

    var itemPanchayat : ArrayList<ItemPanchayat> = ArrayList()
    var panchayatId : Int = 0

    var itemPincode : ArrayList<ItemPincode> = ArrayList()
    var pincodeId : Int = 0

    var currentAddress : String = ""

    fun state(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful){
                        itemState = response.body()?.data as ArrayList<ItemState>
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

    fun district(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("state_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemDistrict>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.district(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemDistrict>>>) {
                    if (response.isSuccessful){
                        itemDistrict = response.body()?.data as ArrayList<ItemDistrict>
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

    fun panchayat(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("state_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemPanchayat>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.panchayat(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemPanchayat>>>) {
                    if (response.isSuccessful){
                        itemPanchayat = response.body()?.data as ArrayList<ItemPanchayat>
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

    fun pincode(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("district_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemPincode>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.pincode(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemPincode>>>) {
                    if (response.isSuccessful){
                        itemPincode = response.body()?.data as ArrayList<ItemPincode>
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







    var itemStateCurrent : ArrayList<ItemState> = ArrayList()
    var stateIdCurrent : Int = 0

    var itemDistrictCurrent : ArrayList<ItemDistrict> = ArrayList()
    var districtIdCurrent : Int = 0

    var itemPanchayatCurrent : ArrayList<ItemPanchayat> = ArrayList()
    var panchayatIdCurrent : Int = 0

    var itemPincodeCurrent : ArrayList<ItemPincode> = ArrayList()
    var pincodeIdCurrent : Int = 0

    var itemLocalOrganizationCurrent : ArrayList<ItemOrganization> = ArrayList()
    var localOrganizationIdCurrent : Int = 0

    var currentAddressCurrent : String = ""

    fun stateCurrent(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful){
                        itemStateCurrent = response.body()?.data as ArrayList<ItemState>
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

    fun districtCurrent(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("state_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemDistrict>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.district(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemDistrict>>>) {
                    if (response.isSuccessful){
                        itemDistrictCurrent = response.body()?.data as ArrayList<ItemDistrict>
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

    fun panchayatCurrent(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("state_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemPanchayat>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.panchayat(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemPanchayat>>>) {
                    if (response.isSuccessful){
                        itemPanchayatCurrent = response.body()?.data as ArrayList<ItemPanchayat>
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

    fun pincodeCurrent(view: View, id: Int) = viewModelScope.launch {
        val obj: JSONObject = JSONObject()
        obj.put("district_id", id)
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemPincode>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.pincode(requestBody = obj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemPincode>>>) {
                    if (response.isSuccessful){
                        itemPincodeCurrent = response.body()?.data as ArrayList<ItemPincode>
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




    fun localOrganisation(view: View, jsonObj: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemOrganization>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.localOrganisation(requestBody = jsonObj.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<List<ItemOrganization>>>) {
                    if (response.isSuccessful){
                        itemLocalOrganizationCurrent = response.body()?.data as ArrayList<ItemOrganization>
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









    var itemVending : ArrayList<ItemVending> = ArrayList()
    var vendingId : Int = 0

    var itemMarketplace : ArrayList<ItemMarketplace> = ArrayList()
    var marketplaceId : Int = 0

    fun vending(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemVending>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.vending()

                override fun success(response: Response<BaseResponseDC<List<ItemVending>>>) {
                    if (response.isSuccessful){
                        itemVending = response.body()?.data as ArrayList<ItemVending>
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


    fun marketplace(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemMarketplace>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.marketplace()

                override fun success(response: Response<BaseResponseDC<List<ItemMarketplace>>>) {
                    if (response.isSuccessful){
                        itemMarketplace = response.body()?.data as ArrayList<ItemMarketplace>
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