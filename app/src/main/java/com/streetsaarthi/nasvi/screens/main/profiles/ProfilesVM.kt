package com.streetsaarthi.nasvi.screens.main.profiles

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemDistrict
import com.streetsaarthi.nasvi.models.mix.ItemMarketplace
import com.streetsaarthi.nasvi.models.mix.ItemOrganization
import com.streetsaarthi.nasvi.models.mix.ItemPanchayat
import com.streetsaarthi.nasvi.models.mix.ItemPincode
import com.streetsaarthi.nasvi.models.mix.ItemState
import com.streetsaarthi.nasvi.models.mix.ItemVending
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.register.RegisterVM
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfilesVM @Inject constructor(private val repository: Repository): ViewModel() {
    var data : Model = Model()

    var itemState : ArrayList<ItemState> = ArrayList()
    var stateId : Int = 0

    var itemDistrict : ArrayList<ItemDistrict> = ArrayList()
    var districtId : Int = 0

    var itemPanchayat : ArrayList<ItemPanchayat> = ArrayList()
    var panchayatId : Int = 0

    var itemPincode : ArrayList<ItemPincode> = ArrayList()
    var pincodeId : String = ""

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







    var itemStateVending : ArrayList<ItemState> = ArrayList()
    var stateIdVending : Int = 0

    var itemDistrictVending : ArrayList<ItemDistrict> = ArrayList()
    var districtIdVending : Int = 0

    var itemPanchayatVending : ArrayList<ItemPanchayat> = ArrayList()
    var panchayatIdVending : Int = 0

    var itemPincodeVending : ArrayList<ItemPincode> = ArrayList()
    var pincodeIdVending : String = ""

    var itemLocalOrganizationVending : ArrayList<ItemOrganization> = ArrayList()
    var localOrganizationIdVending : Int = 0

    var currentAddressCurrent : String = ""

    fun stateCurrent(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful){
                        itemStateVending = response.body()?.data as ArrayList<ItemState>
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
                        itemDistrictVending = response.body()?.data as ArrayList<ItemDistrict>
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
                        itemPanchayatVending = response.body()?.data as ArrayList<ItemPanchayat>
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
                        itemPincodeVending = response.body()?.data as ArrayList<ItemPincode>
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
                        itemLocalOrganizationVending = response.body()?.data as ArrayList<ItemOrganization>
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






    fun profileUpdate(view: View, _id: String, hashMap: RequestBody, value: Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profileUpdate(_id, hashMap)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        //showSnackBar(response.body()?.message.orEmpty())
//                        if(response.body()!!.data != null){
//                            Log.e("TAG", "aaaaaZZ")
//                    }
                        if (value == 111){
                            profile(view, response.body()!!.vendor_id!!, 111)
                        } else if (value == 222){
                            Handler(Looper.getMainLooper()).postDelayed({
                                ProfessionalDetails.callBackListener!!.onCallBack(22)
                            }, 100)
                        } else if (value == 333){
                            profile(view, response.body()!!.vendor_id!!, 111)
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






    fun profile(view: View, _id: String, value: Int) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profile(_id)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        if (value == 111){
                            showSnackBar(response.body()?.message.orEmpty())
                        }

                        if(response.body()!!.data != null){
                            Log.e("TAG", "aaaaa")
                            DataStoreUtil.saveData(
                                DataStoreKeys.AUTH,
                                response.body()!!.token ?: ""
                            )
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LOGIN_DATA,
                                Gson().fromJson(response.body()!!.data, Login::class.java)
                            )
                            Profiles.callBackListener!!.onCallBack(33)
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






    data class Model(
        var vendor_first_name : String ?= null,
        var vendor_last_name : String ?= null,
        var parent_first_name : String ?= null,
        var parent_last_name : String ?= null,
        var gender : String ?= null,
        var date_of_birth : String ?= null,
        var social_category : String ?= null,
        var education_qualification : String ?= null,
        var marital_status : String ?= null,
        var spouse_name : String ?= null,
        var current_state : String ?= null,
        var current_district : String ?= null,
        var municipality_panchayat_current : String ?= null,
        var current_pincode : String ?= null,
        var current_address : String ?= null,

        var passportSizeImage : String ?= null,
        var identificationImage : String ?= null,



        var type_of_marketplace : String ?= null,
        var marketpalce_others : String ?= null,
        var type_of_vending : String ?= null,
        var vending_others : String ?= null,

        var total_years_of_business : String ?= null,

        var open : String ?= null,
        var close : String ?= null,

        var vending_state : String ?= null,
        var vending_district : String ?= null,
        var vending_municipality_panchayat : String ?= null,
        var vending_pincode : String ?= null,
        var vending_address : String ?= null,
        var localOrganisation : String ?= null,


        var shopImage : String ?= null,

        var documentDetails : Boolean ?= false,
        var ImageUploadCOV : String ?= null,
        var ImageUploadLOR : String ?= null,
        var UploadSurveyReceipt : String ?= null,
        var UploadChallan : String ?= null,
        var UploadApprovalLetter : String ?= null,

        var ImageUploadCOVBoolean : Boolean ?= false,
        var ImageUploadLORBoolean : Boolean ?= false,
        var UploadSurveyReceiptBoolean : Boolean ?= false,
        var UploadChallanBoolean : Boolean ?= false,
        var UploadApprovalLetterBoolean : Boolean ?= false,

        var vending_documents : String ?= "null",

        var governmentScheme : Boolean ?= false,
        var pmSwanidhiScheme : Boolean ?= false,
        var otherScheme : Boolean ?= false,
        var schemeName : String ?= null,

        var mobile_no : String ?= null,
        var otp : String ?= null,
        var password : String ?= null
    )


    override fun onCleared() {
        super.onCleared()
//        itemState.clear()
//        itemDistrict.clear()
//        itemPanchayat.clear()
//        itemPincode.clear()
//        itemStateVending.clear()
//        itemDistrictVending.clear()
//        itemPanchayatVending.clear()
//        itemPincodeVending.clear()
//        itemLocalOrganizationVending.clear()
    }

}