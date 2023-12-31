package com.streetsaarthi.screens.onboarding.register

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.mix.ItemDistrict
import com.streetsaarthi.models.mix.ItemMarketplace
import com.streetsaarthi.models.mix.ItemPanchayat
import com.streetsaarthi.models.mix.ItemPincode
import com.streetsaarthi.models.mix.ItemState
import com.streetsaarthi.models.mix.ItemVending
import com.streetsaarthi.networking.getJsonRequestBody
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(private val repository: Repository): ViewModel() {
    var data : Model = Model()

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


    var documentDetails : Boolean ?= false
    var governmentScheme : Boolean ?= false

    var isSend = MutableLiveData<Boolean>(false)
    var isSendMutable = MutableLiveData<Boolean>(false)
    var isOtpVerified = false

    fun sendOTP(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.sendOTP(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        isSend.value = true
                        showSnackBar(response.body()?.message.orEmpty())
                    } else{
                        isSend.value = false
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

    fun verifyOTP(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.verifyOTP(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
//                        if(response.body()?.message == view.rootView.context.getString(R.string.InvalidOTP)){
//                            isOtpVerified = false
                        // showSnackBar(response.body()?.message.orEmpty())
//                        } else {
//                            isOtpVerified = true
//                            showSnackBar(response.body()?.message.orEmpty())
//                        }
                        showSnackBar(response.body()?.message.orEmpty())
                        if(response.body()?.data != null){
                            isOtpVerified = true
                            isSendMutable.value = true
                        } else {
                            isOtpVerified = false
                            isSendMutable.value = false
                        }
                    } else{
                        isOtpVerified = false
                        isSendMutable.value = false
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

    fun onClick(view: View) {
        when (view.id) {

        }
    }



    fun register(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.register(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController().navigate(R.id.action_register_to_registerSuccessful)
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



    fun registerWithFiles(
        view: View,
        hashMap: RequestBody
    ) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.registerWithFiles( hashMap)

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    Log.e("TAG", "responseAA "+response.body().toString())
                    if (response.isSuccessful){
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController().navigate(R.id.action_register_to_registerSuccessful)
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




//    fun uploadImage(imagePath: String, callback: String.() -> Unit) = getAuthToken {
//        viewModelScope.launch {
//            repository.callApi(callHandler = object :
//                CallHandler<Response<BaseResponseDC<UserDataDC>>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.uploadImage(image = File(imagePath).getPartMap("file"))
//
//                override fun success(response: Response<BaseResponseDC<UserDataDC>>) {
//                    if (response.isSuccessful) {
//                        imageUrl = response.body()?.data?.filePath
//                        callback(imageUrl.orEmpty())
//                    }
//                }
//            })
//        }
//    }

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

        var PassportSizeImage : String ?= null,
        var IdentificationImage : String ?= null,



        var type_of_marketplace : String ?= null,
        var type_of_vending : String ?= null,
        var total_years_of_business : String ?= null,

        var open : String ?= null,
        var close : String ?= null,

        var birth_state : String ?= null,
        var birth_district : String ?= null,
        var municipality_panchayat_birth : String ?= null,
        var birth_pincode : String ?= null,
        var birth_address : String ?= null,



        var ShopImage : String ?= null,

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

        var governmentScheme : Boolean ?= false,
        var pmSwanidhiScheme : Boolean ?= false,
        var othersName : Boolean ?= false,
        var schemeName : String ?= null,

        var mobile_no : String ?= null,
        var otp : String ?= null,
        var password : String ?= null
    )


}