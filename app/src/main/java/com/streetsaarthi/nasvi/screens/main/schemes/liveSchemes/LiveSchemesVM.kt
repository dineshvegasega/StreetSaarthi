package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveSchemeBinding
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveTrainingBinding
import com.streetsaarthi.nasvi.databinding.ItemAllSchemesBinding
import com.streetsaarthi.nasvi.databinding.ItemLiveSchemesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemAds
import com.streetsaarthi.nasvi.models.mix.ItemLiveScheme
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.utils.GlideApp
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.myOptionsGlide
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LiveSchemesVM @Inject constructor(private val repository: Repository): ViewModel() {
    init {

    }


    val photosAdapter = object : GenericAdapter<ItemLiveSchemesBinding, ItemLiveScheme>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ItemLiveSchemesBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ItemLiveSchemesBinding, dataClass: ItemLiveScheme, position: Int) {
            binding.apply {
                GlideApp.with(root.context)
                    .load(dataClass.scheme_image.url)
                    .apply(myOptionsGlide)
                    .into(ivMap)
                textTitle.setText(dataClass.name)
                textDesc.setText(dataClass.description)
                textHeaderTxt4.setText(dataClass.status)


                root.setOnClickListener {
                    val dialogBinding = DialogBottomLiveSchemeBinding.inflate(root.context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    dialogBinding.apply {
                        val dialog = BottomSheetDialog(root.context)
                        dialog.setContentView(root)
                        dialog.setOnShowListener { dia ->
                            val bottomSheetDialog = dia as BottomSheetDialog
                            val bottomSheetInternal: FrameLayout =
                                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                        }
                        dialog.show()

                        GlideApp.with(root.context)
                            .load(dataClass.scheme_image.url)
                            .apply(myOptionsGlide)
                            .into(ivMap)
                        textTitle.setText(dataClass.name)
                        textDesc.setText(dataClass.description)
                        textHeaderTxt4.setText(dataClass.status)


                        btClose.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }





    private var itemLiveSchemesResult = MutableLiveData< ArrayList<ItemLiveScheme>>()
    val itemLiveSchemes : LiveData<ArrayList<ItemLiveScheme>> get() = itemLiveSchemesResult
    fun liveScheme(view: View, jsonObject: JSONObject) = viewModelScope.launch {
        repository.callApi(
            callHandler = object : CallHandler<Response<BaseResponseDC<JsonElement>>> {
                override suspend fun sendRequest(apiInterface: ApiInterface) =
                    apiInterface.liveScheme(requestBody = jsonObject.getJsonRequestBody())
                override fun success(response: Response<BaseResponseDC<JsonElement>>) {
                    if (response.isSuccessful){
                        val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
                        val changeValue = Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(response.body()!!.data), typeToken)
                        itemLiveSchemesResult.value = changeValue as ArrayList<ItemLiveScheme>
//                        itemLiveSchemesResult.value = null
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