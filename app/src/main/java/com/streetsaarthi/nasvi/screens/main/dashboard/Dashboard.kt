package com.streetsaarthi.nasvi.screens.main.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.databinding.DialogBottomLiveTrainingBinding
import com.streetsaarthi.nasvi.databinding.DialogBottomNetworkBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import org.json.JSONObject

@AndroidEntryPoint
class Dashboard : Fragment() {
    private val viewModel: DashboardVM by viewModels()
    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!

    var networkAlert : BottomSheetDialog?= null

    var networkCount = 1

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

            if(networkFailed) {
                callApis()
            } else {
                requireContext().callNetworkDialog()
            }



            viewModel.counterNetwork.observe(viewLifecycleOwner, Observer {
                if (it) {
                    if(networkCount == 1){
                        if(networkAlert?.isShowing == true) {
                            return@Observer
                        }
                        val dialogBinding = DialogBottomNetworkBinding.inflate(root.context.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        )
                        networkAlert = BottomSheetDialog(root.context)
                        networkAlert?.setContentView(dialogBinding.root)
                        networkAlert?.setOnShowListener { dia ->
                            val bottomSheetDialog = dia as BottomSheetDialog
                            val bottomSheetInternal: FrameLayout =
                                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                            bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                        }
                        networkAlert?.show()

                        dialogBinding.apply {
                            btClose.singleClick {
                                networkAlert?.dismiss()
                            }
                            btApply.singleClick {
                                networkAlert?.dismiss()
                                callApis()
                            }
                        }
                    }
                    networkCount++
                }
            })


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

    private fun callApis() {
        networkCount = 1
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                var _id = Gson().fromJson(loginUser, Login::class.java).id
                val obj: JSONObject = JSONObject().apply {
                    put("page", "1")
                    put("status", "Active")
                    put("user_id", _id)
                }
                viewModel.liveScheme(view = requireView(), obj)
                viewModel.liveTraining(view = requireView(), obj)
                viewModel.liveNotice(view = requireView(), obj)
                val obj2: JSONObject = JSONObject().apply {
                    put("user_id", _id)
                }
                viewModel.complaintFeedbackHistory(view = requireView(), obj2)
                viewModel.informationCenter(view = requireView(), obj)
                viewModel.profile(view = requireView(), ""+Gson().fromJson(loginUser, Login::class.java).id)
            }
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