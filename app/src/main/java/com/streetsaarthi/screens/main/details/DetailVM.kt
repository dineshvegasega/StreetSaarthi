package com.demo.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.Repository
import com.streetsaarthi.databinding.ListItemBinding
import com.streetsaarthi.models.ItemContributors
import com.streetsaarthi.models.ItemContributorsItem
import com.squareup.picasso.Picasso

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val photosAdapter = object : GenericAdapter<ListItemBinding, ItemContributorsItem>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ListItemBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ListItemBinding, dataClass: ItemContributorsItem, position: Int) {
            binding.txtTitle.text = dataClass.login
            Picasso.get().load(
                dataClass.avatar_url
            ).into(binding!!.ivIcon)

        }
    }





    private var result = MutableLiveData<ItemContributors>()
    val readResult : LiveData<ItemContributors> get() = result

//    fun getContributors(login: String?, name: String?){
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<ItemContributors>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        apiInterface.getContributors(login!!,name!!)
//
//                    override fun success(response: Response<ItemContributors>) {
//                        if (response.isSuccessful){
//                            result.value = response.body()
//                        }
//                    }
//
//                    override fun error(message: String) {
//                        super.error(message)
//                    }
//
//                    override fun loading() {
//                        super.loading()
//                    }
//                }
//            )
//        }
//
//
//    }
}