package com.streetsaarthi.nasvi.screens.main.schemes.allSchemes

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.AllSchemesBinding
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemLiveScheme
import com.streetsaarthi.nasvi.screens.main.dashboard.DashboardVM
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.CheckValidation
import com.streetsaarthi.nasvi.utils.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class AllSchemes : Fragment() {
    private val viewModel: AllSchemesVM by viewModels()
    private var _binding: AllSchemesBinding? = null
    private val binding get() = _binding!!

    private var pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllSchemesBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.all_schemes)

            loadFirstPage()
            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.adapter
            binding.recyclerView.itemAnimator = DefaultItemAnimator()

            observerDataRequest()

            recyclerViewScroll()

            inclideHeaderSearch.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadFirstPage()
                }
                true
            }

        }
    }



    private fun recyclerViewScroll() {
        binding.apply {
            recyclerView.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage += 1
                    if(totalPages >= currentPage){
                        Handler(Looper.myLooper()!!).postDelayed({
                            loadNextPage()
                        }, 1000)
                    }
                }
                override fun getTotalPageCount(): Int {
                    return totalPages
                }
                override fun isLastPage(): Boolean {
                    return isLastPage
                }
                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
        }
    }


    private fun loadFirstPage() {
        pageStart  = 1
        isLoading = false
        isLastPage = false
        totalPages  = 1
        currentPage  = pageStart
        results.clear()
        if (CheckValidation.isConnected(requireContext())) {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val obj: JSONObject = JSONObject().apply {
                        put("page", currentPage)
//                        put("status", "Active")
                        put("search_input", binding.inclideHeaderSearch.editTextSearch.text.toString())
                        put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                    }
                    viewModel.liveScheme(view = requireView(), obj)
                }
            }
        }
    }

    fun loadNextPage() {
        Log.e("TAG", "loadNextPage "+currentPage)
        if (CheckValidation.isConnected(requireContext())) {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val obj: JSONObject = JSONObject().apply {
                        put("page", currentPage)
//                        put("status", "Active")
                        put("search_input", binding.inclideHeaderSearch.editTextSearch.text.toString())
                        put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                    }
                    viewModel.liveSchemeSecond(view = requireView(), obj)
                }
            }
        }
    }



    var results: MutableList<ItemLiveScheme> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    private fun observerDataRequest(){
        viewModel.itemLiveSchemes.observe(requireActivity()) {
            val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
            val changeValue = Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(it.data), typeToken)
            results.addAll(changeValue as MutableList<ItemLiveScheme>)
            viewModel.adapter.addAllSearch(results)

            totalPages = it.meta?.total_pages!!
            if (currentPage == totalPages) {
                viewModel.adapter.removeLoadingFooter()
            } else if (currentPage <= totalPages) {
                viewModel.adapter.addLoadingFooter()
                isLastPage = false
            } else {
                isLastPage = true
            }

            if (viewModel.adapter.itemCount > 0) {
                binding.idDataNotFound.root.visibility = View.GONE
            } else {
                binding.idDataNotFound.root.visibility = View.VISIBLE
            }
        }

        viewModel.itemLiveSchemesSecond.observe(requireActivity()) {
            val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
            val changeValue = Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(it.data), typeToken)
            results.addAll(changeValue as MutableList<ItemLiveScheme>)
            viewModel.adapter.removeLoadingFooter()
            isLoading = false
            viewModel.adapter.addAllSearch(results)
            if (currentPage != totalPages) viewModel.adapter.addLoadingFooter()
            else isLastPage = true
        }


        viewModel.applyLink.observe(requireActivity()) { position ->
            if (position != -1){
                var data = results.get(position).apply {
                    user_scheme_status = "applied"
                }
                viewModel.adapter.notifyDataSetChanged()
                viewModel.viewDetail(""+data.scheme_id, position = position, requireView(), 3)
            }
        }
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}