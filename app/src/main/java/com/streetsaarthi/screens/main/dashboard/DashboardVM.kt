package com.streetsaarthi.screens.main.dashboard

import android.animation.ValueAnimator
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
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ItemDashboardMenusBinding
import com.streetsaarthi.databinding.ItemRecentActivitiesBinding
import com.streetsaarthi.datastore.DataStoreKeys
import com.streetsaarthi.datastore.DataStoreUtil
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.mix.ItemLiveNotice
import com.streetsaarthi.models.mix.ItemLiveScheme
import com.streetsaarthi.models.mix.ItemLiveTraining
import com.streetsaarthi.networking.getJsonRequestBody
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.RawValue
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class DashboardVM @Inject constructor(private val repository: Repository): ViewModel() {


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

        override fun onBindHolder(binding: ItemDashboardMenusBinding, dataClass: ItemModel, position: Int) {
            binding.apply {
                val anim = ValueAnimator.ofFloat(1f, 0.8f)
                if(dataClass.isNew == true){
                    anim.setDuration(1000)
                    anim.addUpdateListener { animation ->
                        ivLogo.setScaleX(animation.animatedValue as Float)
                        ivLogo.setScaleY(animation.animatedValue as Float)
                    }
                    anim.repeatCount = 500
                    anim.repeatMode = ValueAnimator.REVERSE
                    anim.start()
                }

                textHeaderadfdsfTxt.setText(dataClass.name)
                ivLogo.setImageResource(dataClass.image)
                root.setOnClickListener {
                    when (position) {
                        0 -> it.findNavController().navigate(R.id.action_dashboard_to_profile)
                        1 -> it.findNavController().navigate(R.id.action_dashboard_to_liveSchemes)
                        2 -> it.findNavController().navigate(R.id.action_dashboard_to_liveNotices)
                        3 -> it.findNavController().navigate(R.id.action_dashboard_to_liveTraining)
                        4 -> it.findNavController().navigate(R.id.action_dashboard_to_history)
                        5 -> it.findNavController().navigate(R.id.action_dashboard_to_informationCenter)
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
            holder.itemView.setOnClickListener {
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
//                        isScheme.value = true
                        val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(response.body()!!.data), typeToken)
//                        readResult.value.apply {
//                            changeValue.listIterator()
//                        }

                        DataStoreUtil.readData(DataStoreKeys.LIVE_SCHEME_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveScheme>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    Log.e("TAG", "responseAA11AA ")
                                    isScheme.value = true
                                } else {
                                    Log.e("TAG", "responseAA22AA ")
                                }
                            }
                            Log.e("TAG", "responseAA33AA ")
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LIVE_SCHEME_DATA, changeValue)
                        }
                    } else{

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



    var isNotice = MutableLiveData<Boolean>(false)
    fun liveNotice(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveNotice(requestBody = jsonObject.getJsonRequestBody())

                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
//                        isNotice.value = true
                        val typeToken = object : TypeToken<List<ItemLiveNotice>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveNotice>>(Gson().toJson(response.body()!!.data), typeToken)
//                        readResult.value.apply {
//                            changeValue.listIterator()
//                        }

                        DataStoreUtil.readData(DataStoreKeys.LIVE_NOTICE_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveNotice>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    Log.e("TAG", "responseAA11AA ")
                                    isNotice.value = true
                                }
                            }
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LIVE_NOTICE_DATA, changeValue)
                        }
                    } else{

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
//                        readResult.value.apply {
//                            changeValue.listIterator()
//                        }

                        DataStoreUtil.readData(DataStoreKeys.LIVE_TRAINING_DATA) { loginUser ->
                            if (loginUser != null) {
                                val savedValue = Gson().fromJson<List<ItemLiveTraining>>(loginUser, typeToken)
                                if(changeValue!= savedValue){
                                    Log.e("TAG", "responseAA11AA ")
                                    isTraining.value = true
                                }
                            }
                            DataStoreUtil.saveObject(
                                DataStoreKeys.LIVE_TRAINING_DATA, changeValue)
                        }
                    } else{

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

data class ItemModel (
    var name: String = "",
    var image: Int = 0,
    var isNew: Boolean = false,
    val items: @RawValue ArrayList<String> ?= ArrayList(),
)