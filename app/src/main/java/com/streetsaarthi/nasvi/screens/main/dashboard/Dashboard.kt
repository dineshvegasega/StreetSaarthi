package com.streetsaarthi.nasvi.screens.main.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemes
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.autoScrollStop
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class Dashboard : Fragment() {
    private val viewModel: DashboardVM by viewModels()
    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val menuHost: MenuHost = requireActivity()
        //createMenu(menuHost)
        MainActivity.mainActivity.get()?.callFragment(1)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = viewModel.dashboardAdapter
            viewModel.isScheme.observe(viewLifecycleOwner, Observer {
                Log.e("TAG","isScheme "+it)
                if (it) {
                    viewModel.itemMain?.get(1)?.apply {
                        isNew = true
                    }
                } else {
                    viewModel.itemMain?.get(1)?.apply {
                        isNew = false
                    }
                }
                viewModel.dashboardAdapter.notifyDataSetChanged()
            })
            viewModel.isNotice.observe(viewLifecycleOwner, Observer {
                if (it) {
                    viewModel.itemMain?.get(2)?.apply {
                        isNew = true
                    }
                } else {
                    viewModel.itemMain?.get(2)?.apply {
                        isNew = false
                    }
                }
                viewModel.dashboardAdapter.notifyDataSetChanged()
            })
            viewModel.isTraining.observe(viewLifecycleOwner, Observer {
                if (it) {
                    viewModel.itemMain?.get(3)?.apply {
                        isNew = true
                    }
                } else {
                    viewModel.itemMain?.get(3)?.apply {
                        isNew = false
                    }
                }
                viewModel.dashboardAdapter.notifyDataSetChanged()
            })
            viewModel.isComplaintFeedback.observe(viewLifecycleOwner, Observer {
                if (it) {
                    viewModel.itemMain?.get(4)?.apply {
                        isNew = true
                    }
                } else {
                    viewModel.itemMain?.get(4)?.apply {
                        isNew = false
                    }
                }
                viewModel.dashboardAdapter.notifyDataSetChanged()
            })
            viewModel.isInformationCenter.observe(viewLifecycleOwner, Observer {
                if (it) {
                    viewModel.itemMain?.get(5)?.apply {
                        isNew = true
                    }
                } else {
                    viewModel.itemMain?.get(5)?.apply {
                        isNew = false
                    }
                }
                viewModel.dashboardAdapter.notifyDataSetChanged()
            })
            viewModel.dashboardAdapter.notifyDataSetChanged()
            viewModel.dashboardAdapter.submitList(viewModel.itemMain)

            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    var _id = Gson().fromJson(loginUser, Login::class.java).id
                    val obj: JSONObject = JSONObject().apply {
                        put("page", "1")
                        put("status", "Active")
                        put("user_id", _id)
                    }
                    viewModel.liveScheme(view = view, obj)
                    viewModel.liveTraining(view = view, obj)
                    viewModel.liveNotice(view = view, obj)
                    val obj2: JSONObject = JSONObject().apply {
                        put("user_id", _id)
                    }
                    viewModel.complaintFeedbackHistory(view = view, obj2)
                    viewModel.informationCenter(view = view, obj)
                    viewModel.profile(view = view, ""+Gson().fromJson(loginUser, Login::class.java).id)
                }
            }



//            viewModel.adsList(view)
//            val adapter = BannerViewPagerAdapter(requireContext())
//
//            viewModel.itemAds.observe(viewLifecycleOwner, Observer {
//                if (it != null) {
//                    viewModel.itemAds.value?.let { it1 ->
//                        adapter.submitData(it1)
//                        banner.adapter = adapter
//                        tabDots.setupWithViewPager(banner, true)
//                        banner.autoScroll()
//                    }
//                }
//            })
        }
    }


    override fun onStop() {
        super.onStop()
        binding.apply {
//            banner.autoScrollStop()
        }
    }


    override fun onStart() {
        super.onStart()
//        LiveSchemes.isReadLiveSchemes = false
    }
    override fun onDestroyView() {
        _binding = null
//        LiveSchemes.isReadLiveSchemes = false
        super.onDestroyView()
    }
}