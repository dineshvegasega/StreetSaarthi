package com.streetsaarthi.nasvi.screens.main.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.streetsaarthi.nasvi.ApiInterface
import com.streetsaarthi.nasvi.CallHandler
import com.streetsaarthi.nasvi.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemDashboardMenusBinding
import com.streetsaarthi.nasvi.databinding.ItemRecentActivitiesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.chat.ItemChat
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemHistory
import com.streetsaarthi.nasvi.models.mix.ItemInformationCenter
import com.streetsaarthi.nasvi.models.mix.ItemLiveNotice
import com.streetsaarthi.nasvi.models.mix.ItemLiveScheme
import com.streetsaarthi.nasvi.models.mix.ItemLiveTraining
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.main.complaintsFeedback.history.History
import com.streetsaarthi.nasvi.screens.main.informationCenter.InformationCenter
import com.streetsaarthi.nasvi.screens.main.notices.liveNotices.LiveNotices
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemes
import com.streetsaarthi.nasvi.screens.main.training.liveTraining.LiveTraining
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.NETWORK_DIALOG_SHOW
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.RawValue
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class DashboardVM @Inject constructor(private val repository: Repository): ViewModel() {

    var counterNetwork = MutableLiveData<Boolean>(false)

    var itemMain : ArrayList<ItemModel> ?= ArrayList()
    init {
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.profiles),
                R.drawable.item_profile
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.live_schemes),
                R.drawable.item_live_scheme
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.notices),
                R.drawable.item_live_notices
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.training),
                R.drawable.item_live_training
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.complaints_feedback),
                R.drawable.item_feedback
            )
        )
        itemMain?.add(
            ItemModel(
                MainActivity.context.get()!!.getString(R.string.information_center),
                R.drawable.information_center
            )
        )
    }

    val dashboardAdapter = object : GenericAdapter<ItemDashboardMenusBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemDashboardMenusBinding.inflate(inflater, parent, false)

        @SuppressLint("SuspiciousIndentation")
        override fun onBindHolder(binding: ItemDashboardMenusBinding, dataClass: ItemModel, position: Int) {
            binding.apply {
//                if(dataClass.isNew == true){
//                    animationView.visibility = View.VISIBLE
//                    textDotTxt.visibility = View.VISIBLE
//                    layoutBottomRed.visibility = View.VISIBLE
//                } else {
//                    animationView.visibility = View.GONE
//                    textDotTxt.visibility = View.GONE
//                    layoutBottomRed.visibility = View.GONE
//                }
                if(position != 0){
                    animationView.visibility = View.VISIBLE
                    textDotTxt.visibility = View.VISIBLE
                    layoutBottomRed.visibility = View.VISIBLE
                } else {
                    animationView.visibility = View.GONE
                    textDotTxt.visibility = View.GONE
                    layoutBottomRed.visibility = View.GONE
                }
                textHeaderTxt.setText(dataClass.name)
                ivLogo.setImageResource(dataClass.image)
                root.singleClick {
                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val data = Gson().fromJson(loginUser, Login::class.java)
                               when (data.status) {
                                   "approved" -> {
                                       when (position) {
                                           0 -> root.findNavController().navigate(R.id.action_dashboard_to_profile)
                                           1 -> root.findNavController().navigate(R.id.action_dashboard_to_liveSchemes)
                                           2 -> root.findNavController().navigate(R.id.action_dashboard_to_liveNotices)
                                           3 -> root.findNavController().navigate(R.id.action_dashboard_to_liveTraining)
                                           4 -> root.findNavController().navigate(R.id.action_dashboard_to_history)
                                           5 -> root.findNavController().navigate(R.id.action_dashboard_to_informationCenter)
                                       }
                                   }
                                   "unverified" -> {
                                       when (position) {
                                           0 -> root.findNavController().navigate(R.id.action_dashboard_to_profile)
                                           else -> showSnackBar(root.resources.getString(R.string.registration_processed))
                                       }
                                   }
                                   "pending" -> {
                                       when (position) {
                                           0 -> root.findNavController().navigate(R.id.action_dashboard_to_profile)
                                           else -> showSnackBar(root.resources.getString(R.string.registration_processed))
                                       }
                                   }
                                   "rejected" -> {
                                       when (position) {
                                           0 -> root.findNavController().navigate(R.id.action_dashboard_to_profile)
                                           else -> showSnackBar(root.resources.getString(R.string.registration_processed))
                                       }
                                   }
                               }
                        }
                    }

                }
            }

        }
    }


    val recentAdapter = object : GenericAdapter<ItemRecentActivitiesBinding, ItemModel>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemRecentActivitiesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemRecentActivitiesBinding, dataClass: ItemModel, position: Int) {
            binding.apply {
                recyclerViewRecentItems.setHasFixedSize(true)
                val headlineAdapter = RecentChildAdapter(
                    binding.root.context,
                    dataClass.items,
                    position
                )
                recyclerViewRecentItems.adapter = headlineAdapter
                recyclerViewRecentItems.layoutManager = LinearLayoutManager(binding.root.context)
            }
        }
    }




    class RecentChildAdapter(context: Context, data: List<String>?, mainPosition: Int) :
        RecyclerView.Adapter<RecentChildAdapter.ChildViewHolder>() {
        private var items: List<String>? = data
        private var inflater: LayoutInflater = LayoutInflater.from(context)
        private var parentPosition: Int = mainPosition

        override
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
            val view = inflater.inflate(R.layout.item_recent_activities_items, parent, false)
            return ChildViewHolder(view)
        }

        override
        fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
//            val item = items?.get(position)
            //holder.tvTitle.text = item?.title
            holder.itemView.singleClick {
            }
        }

        override
        fun getItemCount(): Int {
//            return items?.size?:0
            return 3
        }

        class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            var tvTitle: AppCompatTextView = itemView.findViewById(R.id.titleChild)
        }
    }




    var isScheme = MutableLiveData<Boolean>(false)
    fun liveScheme(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(response.body()!!.data), typeToken)
                        DataStoreUtil.readData(DataStoreKeys.LIVE_SCHEME_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveScheme>>(loginUser, typeToken)
                                if(changeValue != savedValue){
                                    isScheme.value = true
                                } else {
                                    isScheme.value = false
                                }
                            } else {
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_SCHEME_DATA, changeValue)
                                isScheme.value = false
                            }
//                            Log.e("TAG", "LiveSchemes.isReadLiveSchemes"+LiveSchemes.isReadLiveSchemes)
                            if (LiveSchemes.isReadLiveSchemes == true){
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_SCHEME_DATA, changeValue)
                                isScheme.value = false
                                LiveSchemes.isReadLiveSchemes = false
                            }

                        }
                    }
                }

                override fun error(message: String) {
                   // super.error(message)
                  //  showSnackBar(message)
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



    var isNotice = MutableLiveData<Boolean>(false)
    fun liveNotice(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveNotice(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val typeToken = object : TypeToken<List<ItemLiveNotice>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveNotice>>(Gson().toJson(response.body()!!.data), typeToken)
                        DataStoreUtil.readData(DataStoreKeys.LIVE_NOTICE_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveNotice>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    isNotice.value = true
                                } else {
                                    isNotice.value = false
                                }
                            } else {
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_NOTICE_DATA, changeValue)
                                isNotice.value = false
                            }
                            if (LiveNotices.isReadLiveNotices == true){
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_NOTICE_DATA, changeValue)
                                isNotice.value = false
                                LiveNotices.isReadLiveNotices = false
                            }
                        }
                    }
                }

                override fun error(message: String) {
                    super.error(message)
                 //   showSnackBar(message)
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



    var isTraining = MutableLiveData<Boolean>(false)
    fun liveTraining(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveTraining(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        //isTraining.value = true
                        val typeToken = object : TypeToken<List<ItemLiveTraining>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveTraining>>(Gson().toJson(response.body()!!.data), typeToken)
                        DataStoreUtil.readData(DataStoreKeys.LIVE_TRAINING_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveTraining>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    isTraining.value = true
                                } else {
                                    isTraining.value = false
                                }
                            }  else {
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_TRAINING_DATA, changeValue)
                                isTraining.value = false
                            }

                            if (LiveTraining.isReadLiveTraining == true){
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.LIVE_TRAINING_DATA, changeValue)
                                isTraining.value = false
                                LiveTraining.isReadLiveTraining = false
                            }
                        }
                    }
                }

                override fun error(message: String) {
                   // super.error(message)
                 //   showSnackBar(message)
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



    var isComplaintFeedback = MutableLiveData<Boolean>(false)
    fun complaintFeedbackHistory(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.complaintFeedbackHistory(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val typeToken = object : TypeToken<List<ItemHistory>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemHistory>>(Gson().toJson(response.body()!!.data), typeToken)
                        changeValue.map {
//                            runBlocking {
//                                feedbackConversationDetails(view, ""+it.feedback_id)
//                            }
//                            Log.e("TAG", "aaaaa_idXXX ")
//                            val call1 = async { feedbackConversationDetails(view, ""+it.feedback_id)}
//                            val data1Response:BaseResponse<Data1>?
//                            try {
//                                data1Response = call1.await()
//
//                            } catch (ex: Exception) {
//                                ex.printStackTrace()
//                            }

//                            feedbackConversationDetails(view, ""+it.feedback_id){
//                                Log.e("TAG", "aaaaa_idZZZ "+it.feedback_id)
//                                Log.e("TAG", "aaaaa_idXXX "+this.toString())
//                            }
                        }
//                        feedbackConversationDetails(view, "82"){
////                            Log.e("TAG", "aaaaa_idZZZ "+it.feedback_id)
//                            Log.e("TAG", "aaaaa_idXXX "+this.toString())
//                        }
                        DataStoreUtil.readData(DataStoreKeys.Complaint_Feedback_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemHistory>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    isComplaintFeedback.value = true
                                } else {
                                    isComplaintFeedback.value = false
                                }
                            }  else {
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.Complaint_Feedback_DATA, changeValue)
                                isComplaintFeedback.value = false
                            }

                            if (History.isReadComplaintFeedback == true){
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.Complaint_Feedback_DATA, changeValue)
                                isComplaintFeedback.value = false
                                History.isReadComplaintFeedback = false
                            }
                        }
                    }
                }

                override fun error(message: String) {
                  //  super.error(message)
               //     showSnackBar(message)
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



//    private var feedbackConversationLiveData = MutableLiveData<ItemChat>()
//    val feedbackConversationLive : LiveData<ItemChat> get() = feedbackConversationLiveData
    fun feedbackConversationDetails(view: View, _id: String, callBack: ItemChat.() -> Unit) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<ItemChat>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.feedbackConversationDetails(_id)
                override fun success(response: Response<ItemChat>) {
                    if (response.isSuccessful){
                        callBack(response.body()!!)
//                        if(response.body()!!.data != null){
//                            Log.e("TAG", "aaaaa "+response.body()!!.data.toString())
//                            Log.e("TAG", "aaaaa_id "+_id)
//
////                            feedbackConversationLiveData.value = response.body()
//                        }
                    }
                }
                override fun error(message: String) {
                   // super.error(message)
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



    var isInformationCenter = MutableLiveData<Boolean>(false)
    fun informationCenter(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.informationCenter(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        //isTraining.value = true
                        val typeToken = object : TypeToken<List<ItemInformationCenter>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemInformationCenter>>(Gson().toJson(response.body()!!.data), typeToken)
                        DataStoreUtil.readData(DataStoreKeys.Information_Center_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemInformationCenter>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    isInformationCenter.value = true
                                } else {
                                    isInformationCenter.value = false
                                }
                            }  else {
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.Information_Center_DATA, changeValue)
                                isInformationCenter.value = false
                            }

                            if (InformationCenter.isReadInformationCenter == true){
                                DataStoreUtil.saveObject(
                                    DataStoreKeys.Information_Center_DATA, changeValue)
                                isInformationCenter.value = false
                                InformationCenter.isReadInformationCenter = false
                            }
                        }
                    }
                }

                override fun error(message: String) {
                  //  super.error(message)
           //         showSnackBar(message)
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



//
//    private var itemAdsResult = MutableLiveData< ArrayList<ItemAds>>()
//    val itemAds : LiveData< ArrayList<ItemAds>> get() = itemAdsResult
//    fun adsList(view: View) = viewModelScope.launch {
//        repository.callApi(
//            callHandler = object : CallHandler<Response<BaseResponseDC<List<ItemAds>>>> {
//                override suspend fun sendRequest(apiInterface: ApiInterface) =
//                    apiInterface.adsList()
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



    fun profile(view: View, _id: String) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.profile(_id)

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        if(response.body()!!.data != null){
                            DataStoreUtil.saveData(
                                DataStoreKeys.AUTH,
                                response.body()!!.token ?: ""
                            )
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LOGIN_DATA,
                                Gson().fromJson(response.body()!!.data, Login::class.java)
                            )
                        }
                    }
                }
                override fun error(message: String) {
                   // super.error(message)
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



}

data class ItemModel (
    var name: String = "",
    var image: Int = 0,
    var isNew: Boolean = false,
    val items: @RawValue ArrayList<String> ?= ArrayList(),
)