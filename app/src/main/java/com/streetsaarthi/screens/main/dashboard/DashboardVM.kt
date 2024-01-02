package com.streetsaarthi.screens.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ListItemBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.models.Items
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(private val repository: Repository): ViewModel() {

    val photosAdapter = object : GenericAdapter<ListItemBinding, Item>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ListItemBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ListItemBinding, dataClass: Item, position: Int) {
            binding.txtTitle.text = dataClass.name
            Picasso.get().load(
                dataClass.owner.avatar_url
            ).into(binding!!.ivIcon)
            binding.root.setOnClickListener {
                it.findNavController().navigate(R.id.detail, Bundle().apply {
                    putParcelable("data", dataClass)
                })
            }
        }
    }


    private var result = MutableLiveData<Items>()
    val readResult : LiveData<Items> get() = result

//    fun getProducts(query: String?){
//        viewModelScope.launch {
//            repository.callApi(
//                callHandler = object : CallHandler<Response<Items>> {
//                    override suspend fun sendRequest(apiInterface: ApiInterface) =
//                        apiInterface.getItems(""+query)
//                    override fun success(response: Response<Items>) {
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