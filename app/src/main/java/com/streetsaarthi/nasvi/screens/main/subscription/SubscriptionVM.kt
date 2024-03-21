package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomSubscriptionBinding
import com.streetsaarthi.nasvi.databinding.ItemSubscriptionHistoryBinding
import com.streetsaarthi.nasvi.genericAdapter.GenericAdapter
import com.streetsaarthi.nasvi.models.BaseResponseDC
import com.streetsaarthi.nasvi.models.ItemSubscription
import com.streetsaarthi.nasvi.networking.ApiInterface
import com.streetsaarthi.nasvi.networking.CallHandler
import com.streetsaarthi.nasvi.networking.Repository
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SubscriptionVM @Inject constructor(private val repository: Repository) : ViewModel() {

    var itemMain: ArrayList<ItemModel>? = ArrayList()

    init {
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
        itemMain?.add(ItemModel())
    }


    val historyAdapter = object : GenericAdapter<ItemSubscriptionHistoryBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemSubscriptionHistoryBinding.inflate(inflater, parent, false)

        @SuppressLint("SuspiciousIndentation")
        override fun onBindHolder(
            binding: ItemSubscriptionHistoryBinding,
            dataClass: ItemModel,
            position: Int
        ) {
            binding.apply {
                layoutMain.backgroundTintList =
                    (if (position % 2 == 0) ContextCompat.getColorStateList(
                        root.context,
                        R.color._f6dbbb
                    ) else ContextCompat.getColorStateList(root.context, R.color.white))
                textSno.setText(dataClass.s_no)
                textDate.setText(dataClass.date)
                textTransactionId.setText(dataClass.transactionId)
                textDuration.setText(dataClass.duration + " " + root.resources.getString(R.string.months))
                root.singleClick {
                    val dialogBinding = DialogBottomSubscriptionBinding.inflate(
                        root.context.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE
                        ) as LayoutInflater
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
                        imageCross.singleClick {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }


    var membershipCost: Double = 0.0
    var validity: String = ""
    var validityMonths: Int = 0
    var validityDays: Int = 0
    var gst: Double = 18.0
    var afterGst: Double = 0.0
    var couponDiscount: Double = 0.0
    var totalCost: Double = 0.0
    var monthYear: Int = 0
    var number: Int = 0

    var subscription = MutableLiveData<ItemSubscription>()
    fun subscription(jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.subscription(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.data != null) {
                            subscription.value = Gson().fromJson(
                                response.body()!!.data,
                                ItemSubscription::class.java
                            )
                        }
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    //  showSnackBar(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


    var purchaseSubscription = MutableLiveData<ItemSubscription>()
    fun purchaseSubscription(jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.purchaseSubscription(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.data != null) {
                            purchaseSubscription.value = Gson().fromJson(
                                response.body()!!.data,
                                ItemSubscription::class.java
                            )
                        }
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    //  showSnackBar(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


    var subscriptionHistory = MutableLiveData<ItemSubscription>()
    fun subscriptionHistory(jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.subscriptionHistory(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.data != null) {
                            subscriptionHistory.value = Gson().fromJson(
                                response.body()!!.data,
                                ItemSubscription::class.java
                            )
                        }
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    //  showSnackBar(message)
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


    var couponLiveListCalled = MutableLiveData<Boolean>(false)
    private var itemCouponLiveListResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemCouponLiveList: LiveData<BaseResponseDC<Any>> get() = itemCouponLiveListResult
    fun couponLiveList(jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.couponLiveList(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful) {
                        itemCouponLiveListResult.value = response.body() as BaseResponseDC<Any>
                        couponLiveListCalled.value = true
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                    //  showSnackBar(message)
                    couponLiveListCalled.value = false
                }

                override fun loading() {
                    super.loading()
                }
            }
        )
    }


    public override fun onCleared() {
        super.onCleared()
        monthYear = 0
        number = 0
    }


}


data class ItemModel(
    var s_no: String = "01",
    var date: String = "12/02/22",
    var transactionId: String = "#312314",
    var duration: String = "12",
    )