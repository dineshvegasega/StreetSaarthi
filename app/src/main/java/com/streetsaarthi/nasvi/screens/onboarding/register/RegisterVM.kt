package com.streetsaarthi.nasvi.screens.onboarding.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemDistrict
import com.streetsaarthi.nasvi.models.mix.ItemMarketplace
import com.streetsaarthi.nasvi.models.mix.ItemOrganization
import com.streetsaarthi.nasvi.models.mix.ItemPanchayat
import com.streetsaarthi.nasvi.models.mix.ItemPincode
import com.streetsaarthi.nasvi.models.mix.ItemState
import com.streetsaarthi.nasvi.models.mix.ItemVending
import com.streetsaarthi.nasvi.models.test.ItemT
import com.streetsaarthi.nasvi.networking.ApiTranslateInterface
import com.streetsaarthi.nasvi.networking.CallHandlerTranslate
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject


@HiltViewModel
class RegisterVM @Inject constructor(private val repository: Repository) : ViewModel() {
    var data: Model = Model()

    var itemVending: ArrayList<ItemVending> = ArrayList()
    var vendingId: Int = 0

    var itemMarketplace: ArrayList<ItemMarketplace> = ArrayList()
    var marketplaceId: Int = 0


    var isAgree = MutableLiveData<Boolean>(false)

    fun vending(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemVending>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.vending()

                override fun success(response: Response<BaseResponseDC<List<ItemVending>>>) {
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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


    var itemState: ArrayList<ItemState> = ArrayList()
    var stateId: Int = 0

    var itemDistrict: ArrayList<ItemDistrict> = ArrayList()
    var districtId: Int = 0

    var itemPanchayat: ArrayList<ItemPanchayat> = ArrayList()
    var panchayatId: Int = 0

    var itemPincode: ArrayList<ItemPincode> = ArrayList()
    var pincodeId: String = ""

    var currentAddress: String = ""

    fun state(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful) {
                        itemState = response.body()?.data as ArrayList<ItemState>
                        translate(response.body()?.data.toString())
//                        var word = ""
//                        var word = try {
//                            callUrlAndParseResult("en", "hi", response.body()?.data.toString())
//                        } catch (e: java.lang.Exception) {
//                            throw RuntimeException(e)
//                        }
//
//                        var bbb = word?.substring( 1, word.length - 1)
//
//                        var xx = bbb?.split("आइटमस्टेट")
//                        xx?.forEach {
//                            Log.e("TAG", "AAAAAA $it")
//                        }

//                        var xx = Gson().toJson(word)
//                        Log.e("TAG", "AAAAAA $xx")
////                        var obj = JSONArray(word)
////                        Log.e("TAG", "BBBBBB $obj")

//                        val typeToken = object : TypeToken<Any>() {}.type
//                        val changeValue = Gson().fromJson<String>(Gson().toJson(word), typeToken)
//                        Log.e("TAG", "changeValue $changeValue")
//                        var ddd = changeValue[0]
//                        Log.e("TAG", "changeValueddd "+ddd)
//                        Json.parseToJsonElement(jsonString) // To a JsonElement
//                            .jsonObject                     // To a JsonObject
//                            .toMutableMap()

//                        val typeToken = object : TypeToken<List<String>>() {}.type
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

    fun state1(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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


    var itemStateVending: ArrayList<ItemState> = ArrayList()
    var stateIdVending: Int = 0

    var itemDistrictVending: ArrayList<ItemDistrict> = ArrayList()
    var districtIdVending: Int = 0

    var itemPanchayatVending: ArrayList<ItemPanchayat> = ArrayList()
    var panchayatIdVending: Int = 0

    var itemPincodeVending: ArrayList<ItemPincode> = ArrayList()
    var pincodeIdVending: String = ""

    var itemLocalOrganizationVending: ArrayList<ItemOrganization> = ArrayList()
    var localOrganizationIdVending: Int = 0

    var currentAddressVending: String = ""

    fun stateCurrent(view: View) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemState>>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.state()

                override fun success(response: Response<BaseResponseDC<List<ItemState>>>) {
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
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
                    if (response.isSuccessful) {
                        itemLocalOrganizationVending =
                            response.body()?.data as ArrayList<ItemOrganization>
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


    var documentDetails: Boolean? = false
    var governmentScheme: Boolean? = false

    var isSend = MutableLiveData<Boolean>(false)
    var isSendMutable = MutableLiveData<Boolean>(false)
    var isOtpVerified = false

    fun sendOTP(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.sendOTP(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful) {
                        if (response.body()?.message == "OTP Sent successfully") {
                            isSend.value = true
                            var number = jsonObject.getString("mobile_no")
                            showSnackBar(view.resources.getString(R.string.otp_sent, number))
                        } else {
                            isSend.value = false
                            showSnackBar(view.resources.getString(R.string.user_already_exist))
                        }
                    } else {
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
                    if (response.isSuccessful) {
                        if (response.body()?.data != null) {
                            isOtpVerified = true
                            isSendMutable.value = true
                            showSnackBar(view.resources.getString(R.string.otp_Verified_successfully))
                        } else {
                            isOtpVerified = false
                            isSendMutable.value = false
                            showSnackBar(view.resources.getString(R.string.invalid_OTP))
                        }
                    } else {
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
                    if (response.isSuccessful) {
                        showSnackBar(response.body()?.message.orEmpty())
                        Handler(Looper.getMainLooper()).postDelayed({
                            view.findNavController().navigate(
                                R.id.action_register_to_registerSuccessful,
                                Bundle().apply {
                                    putString("key", jsonObject.getString("vendor_first_name"))
                                })
                        }, 100)
                    } else {
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
        hashMap: RequestBody,
        _string: String
    ) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<Any>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.registerWithFiles(hashMap)

                override fun success(response: Response<BaseResponseDC<Any>>) {
                    if (response.isSuccessful) {
                        showSnackBar(response.body()?.message.orEmpty())
                        view.findNavController()
                            .navigate(R.id.action_register_to_registerSuccessful, Bundle().apply {
                                putString("key", _string)
                            })
                    } else {
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


    @Throws(Exception::class)
    private fun callUrlAndParseResult(
        langFrom: String, langTo: String,
        word: String
    ): String? {
        val url = "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx&" +
                "sl=" + langFrom +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8")
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.setRequestProperty("User-Agent", "Mozilla/5.0")
        val `in` = BufferedReader(
            InputStreamReader(con.inputStream)
        )
        var inputLine: String?
        val response = StringBuffer()
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        `in`.close()
        return parseResult(response.toString())
    }

    @Throws(Exception::class)
    private fun parseResult(inputJson: String): String? {
        var words = ""
        /*
         * inputJson for word 'hello' translated to language Hindi from English-
         * [[["नमस्ते","hello",,,1]],,"en"]
         * We have to get 'नमस्ते ' from this json.
         */
        val jsonArray = JSONArray(inputJson)
        val jsonArray2 = jsonArray[0] as JSONArray
        for (i in 0..jsonArray2.length() - 1) {
            val jsonArray3 = jsonArray2[i] as JSONArray
            words += jsonArray3[0].toString()
        }

        return words.toString()
    }


    fun translate(words: String) = viewModelScope.launch {
        repository.callApiTranslate(
            callHandler = object : CallHandlerTranslate<Response<ItemT>> {
                override suspend fun sendRequest(apiInterface: ApiTranslateInterface) =
                    apiInterface.translate(words)

                override fun success(response: Response<ItemT>) {
                    if (response.isSuccessful) {
                        //itemState = response.body()?.data as ArrayList<ItemState>


                        var words = ""
                        val jsonArray = JSONArray(response.body())
                        val jsonArray2 = jsonArray[0] as JSONArray
                        for (i in 0..jsonArray2.length() - 1) {
                            val jsonArray3 = jsonArray2[i] as JSONArray
                            words += jsonArray3[0].toString()
                        }
//                    Log.e("TAG", "XXXX "+words.toString())

                        var hindi = getHindi(words)

                        Log.e("TAG", "AAAAAAhindi ${hindi.toString()}")
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


    private fun getHindi(words: String) : ArrayList<ItemState> {
        var hindiArray: ArrayList<ItemState> = ArrayList()
        var bbb = words?.substring(1, words.length - 1)
        var xx = bbb?.split("आइटमस्टेट")
        xx?.forEach {
            if (it.contains("देश_आईडी") || it.contains("country_id")) {
                var stateObjCountryId = it?.split(", ")?.get(0)
                val country_id = if (stateObjCountryId!!.contains("देश_आईडी")) {
                    stateObjCountryId?.substringAfter("देश_आईडी=")?.substringBefore(",")?.substringBefore(")")
                } else if (stateObjCountryId.contains("country_id")) {
                    stateObjCountryId?.substringAfter("country_id=")?.substringBefore(",")?.substringBefore(")")
                } else {
                    ""
                }
                Log.e("TAG", "AAAAAAid $country_id")


                var stateObjId = it?.split(", ")?.get(1)
                val _id = if (stateObjId!!.contains("आईडी=")) {
                    stateObjId?.substringAfter("आईडी=")
                } else if (stateObjId!!.contains("आईडी =")) {
                    stateObjId?.substringAfter("आईडी =")
                } else if (stateObjId.contains("id")) {
                    stateObjId?.substringAfter("id=")
                } else {
                    ""
                }
                Log.e("TAG", "AAAAAAresult $_id")


                var stateObjName = it?.split(", ")?.get(2)
                val name = if (stateObjName!!.contains("नाम")) {
                    stateObjName?.substringAfter("नाम=")?.substringBefore(",")?.substringBefore(")")
                } else if (stateObjName.contains("name")) {
                    stateObjName?.substringAfter("name=")?.substringBefore(",")?.substringBefore(")")
                } else {
                    ""
                }
                Log.e("TAG", "AAAAAAname $name")
//
                hindiArray.add(ItemState(country_id!!.replace(" ", "").toInt(), _id.replace(" ", "").toInt(), name!!))
            }
        }

        return hindiArray
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
        var vendor_first_name: String? = null,
        var vendor_last_name: String? = null,
        var parent_first_name: String? = null,
        var parent_last_name: String? = null,
        var gender: String? = null,
        var date_of_birth: String? = null,
        var social_category: String? = null,
        var education_qualification: String? = null,
        var marital_status: String? = null,
        var spouse_name: String? = null,
        var current_state: String? = null,
        var current_district: String? = null,
        var municipality_panchayat_current: String? = null,
        var current_pincode: String? = null,
        var current_address: String? = null,

        var passportSizeImage: String? = null,
        var identificationImage: String? = null,


        var type_of_marketplace: String? = null,
        var marketpalce_others: String? = null,
        var type_of_vending: String? = null,
        var vending_others: String? = null,

        var total_years_of_business: String? = null,

        var open: String? = null,
        var close: String? = null,

        var vending_state: String? = null,
        var vending_district: String? = null,
        var vending_municipality_panchayat: String? = null,
        var vending_pincode: String? = null,
        var vending_address: String? = null,
        var localOrganisation: String? = null,


        var shopImage: String? = null,

        var documentDetails: Boolean? = false,
        var ImageUploadCOV: String? = null,
        var ImageUploadLOR: String? = null,
        var UploadSurveyReceipt: String? = null,
        var UploadChallan: String? = null,
        var UploadApprovalLetter: String? = null,

        var ImageUploadCOVBoolean: Boolean? = false,
        var ImageUploadLORBoolean: Boolean? = false,
        var UploadSurveyReceiptBoolean: Boolean? = false,
        var UploadChallanBoolean: Boolean? = false,
        var UploadApprovalLetterBoolean: Boolean? = false,

        var governmentScheme: Boolean? = false,
        var pmSwanidhiScheme: Boolean? = false,
        var otherScheme: Boolean? = false,
        var schemeName: String? = null,

        var mobile_no: String? = null,
        var otp: String? = null,
        var password: String? = null
    )


}