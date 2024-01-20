package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LiveSchemesVM @Inject constructor(private val repository: Repository): ViewModel() {

    val adapter by lazy { LiveSchemesAdapter() }


//    val photosAdapter = object : GenericAdapter<ItemLiveSchemesBinding, ItemLiveScheme>() {
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            parent: ViewGroup,
//            viewType: Int
//        ) = ItemLiveSchemesBinding.inflate(inflater, parent, false)
//
//        override fun onBindHolder(binding: ItemLiveSchemesBinding, dataClass: ItemLiveScheme, position: Int) {
//            binding.apply {
//                GlideApp.with(root.context)
//                    .load(dataClass.scheme_image.url)
//                    .apply(myOptionsGlide)
//                    .into(ivMap)
//                textTitle.setText(dataClass.name)
//                textDesc.setText(dataClass.description)
//                textHeaderTxt4.setText(dataClass.status)
//
//
//                root.setOnClickListener {
//                    val dialogBinding = DialogBottomLiveSchemeBinding.inflate(root.context.getSystemService(
//                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
//                    dialogBinding.apply {
//                        val dialog = BottomSheetDialog(root.context)
//                        dialog.setContentView(root)
//                        dialog.setOnShowListener { dia ->
//                            val bottomSheetDialog = dia as BottomSheetDialog
//                            val bottomSheetInternal: FrameLayout =
//                                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
//                            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
//                        }
//                        dialog.show()
//
//                        GlideApp.with(root.context)
//                            .load(dataClass.scheme_image.url)
//                            .apply(myOptionsGlide)
//                            .into(ivMap)
//                        textTitle.setText(dataClass.name)
//                        textDesc.setText(dataClass.description)
//                        textHeaderTxt4.setText(dataClass.status)
//
//
//                        btClose.setOnClickListener {
//                            dialog.dismiss()
//                        }
//                    }
//                }
//            }
//        }
//
////        fun addLoadingFooter() {
////            isLoadingAdded = true
////            add(ResultsItem())
////        }
//    }





    private var itemLiveSchemesResult = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveSchemes : LiveData<BaseResponseDC<Any>> get() = itemLiveSchemesResult
    fun liveScheme(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveSchemesResult.value = response.body() as BaseResponseDC<Any>
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



    private var itemLiveSchemesResultSecond = MutableLiveData<BaseResponseDC<Any>>()
    val itemLiveSchemesSecond : LiveData<BaseResponseDC<Any>> get() = itemLiveSchemesResultSecond
    fun liveSchemeSecond(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        itemLiveSchemesResultSecond.value =  response.body() as BaseResponseDC<Any>
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