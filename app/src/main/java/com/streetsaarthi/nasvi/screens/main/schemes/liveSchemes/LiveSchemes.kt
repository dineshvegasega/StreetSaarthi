package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.databinding.LiveNoticesBinding
import com.streetsaarthi.nasvi.databinding.LiveSchemesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.main.dashboard.DashboardVM
import com.streetsaarthi.nasvi.utils.autoScroll
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LiveSchemes : Fragment() {
    private val viewModel: LiveSchemesVM by viewModels()
    private var _binding: LiveSchemesBinding? = null
    private val binding get() = _binding!!

    companion object{
        var isReadLiveSchemes: Boolean? = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LiveSchemesBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isReadLiveSchemes = true

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.live_schemes)

            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val obj: JSONObject = JSONObject().apply {
                        put("page", "1")
                        put("status", "Active")
//                    put("search_input", USER_TYPE)
                        put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                    }
                    viewModel.liveScheme(view = view, obj)
                }
            }

            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.photosAdapter

            viewModel.itemLiveSchemes.observe(viewLifecycleOwner, Observer {
                    viewModel.itemLiveSchemes.value?.let { it1 ->
                    viewModel.photosAdapter.notifyDataSetChanged()
                    viewModel.photosAdapter.submitList(it1)
                }
                if (it != null) {
                    idDataNotFound.root.visibility = View.GONE
                } else {
                    idDataNotFound.root.visibility = View.VISIBLE
                }
            })

        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}