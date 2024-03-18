package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.streetsaarthi.nasvi.databinding.SubscriptionHistoryBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class SubscriptionHistory : Fragment(){
    private val viewModel: SubscriptionVM by activityViewModels()
    private var _binding: SubscriptionHistoryBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SubscriptionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(1)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = viewModel.historyAdapter
            viewModel.historyAdapter.submitList(viewModel.itemMain)
            viewModel.historyAdapter.notifyDataSetChanged()



            readData(LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(loginUser, Login::class.java)
                    if (networkFailed) {
                        val obj: JSONObject = JSONObject().apply {
                            put("state_id", data.id)
                        }
                        viewModel.subscriptionHistory(obj)
                    } else {
                        requireContext().callNetworkDialog()
                    }
                }
            }

        }
    }

}