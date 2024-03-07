package com.streetsaarthi.nasvi.screens.main.notices.liveNotices

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomNetworkBinding
import com.streetsaarthi.nasvi.databinding.LiveNoticesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.models.ItemLiveNotice
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.IS_LANGUAGE
import com.streetsaarthi.nasvi.utils.PaginationScrollListener
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.isNetworkAvailable
import com.streetsaarthi.nasvi.utils.mainThread
import com.streetsaarthi.nasvi.utils.onRightDrawableClicked
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.json.JSONObject

@AndroidEntryPoint
class LiveNotices : Fragment() {
    private val viewModel: LiveNoticesVM by viewModels()
    private var _binding: LiveNoticesBinding? = null
    private val binding get() = _binding!!

    companion object{
        var isReadLiveNotices: Boolean? = false
    }

    var networkAlert : BottomSheetDialog?= null
    var networkCount = 1

    private var LOADER_TIME: Long = 500
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
        _binding = LiveNoticesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(1)
        isReadLiveNotices = true
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.live_notices)
            idDataNotFound.textDesc.text = getString(R.string.currently_no_notice)

            loadFirstPage()
            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.adapter
            binding.recyclerView.itemAnimator = DefaultItemAnimator()

            observerDataRequest()

            recyclerViewScroll()

            searchHandler()


        }
    }



    private fun searchHandler() {
        binding.apply {
            inclideHeaderSearch.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadFirstPage()
                }
                true
            }

            inclideHeaderSearch.editTextSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    inclideHeaderSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, if(count >= 1) R.drawable.ic_cross_white else R.drawable.ic_search, 0);
                }
            })

            inclideHeaderSearch.editTextSearch.onRightDrawableClicked {
                it.text.clear()
                loadFirstPage()
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
                        }, LOADER_TIME)
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
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                val obj: JSONObject = JSONObject().apply {
                    put("page", currentPage)
//                        put("status", "Active")
                    put("search_input", binding.inclideHeaderSearch.editTextSearch.text.toString())
                    put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                }
                if(requireContext().isNetworkAvailable()) {
                    viewModel.liveNotice(obj)
                } else {
                    requireContext().callNetworkDialog()
                }
            }
        }
    }

    fun loadNextPage() {
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                val obj: JSONObject = JSONObject().apply {
                    put("page", currentPage)
//                        put("status", "Active")
                    put("search_input", binding.inclideHeaderSearch.editTextSearch.text.toString())
                    put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                }
                if(requireContext().isNetworkAvailable()) {
                    viewModel.liveNoticeSecond(obj)
                } else {
                    requireContext().callNetworkDialog()
                }
            }
        }
    }


    var results: MutableList<ItemLiveNotice> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    private fun observerDataRequest(){
        viewModel.itemLiveNotice.observe(viewLifecycleOwner, Observer {
            viewModel.show()
            val typeToken = object : TypeToken<List<ItemLiveNotice>>() {}.type
            val changeValue =
                Gson().fromJson<List<ItemLiveNotice>>(Gson().toJson(it.data), typeToken)
            if (IS_LANGUAGE){
                if (MainActivity.context.get()!!
                        .getString(R.string.englishVal) == "" + viewModel.locale
                ) {
                    val itemStateTemp = changeValue
                    results.addAll(itemStateTemp)
                    viewModel.adapter.addAllSearch(results)
                    viewModel.hide()

                    if (viewModel.adapter.itemCount > 0) {
                        binding.idDataNotFound.root.visibility = View.GONE
                    } else {
                        binding.idDataNotFound.root.visibility = View.VISIBLE
                    }
                } else {
                    val itemStateTemp = changeValue
                    mainThread {
                        itemStateTemp.forEach {
                            delay(50)
                            val nameChanged: String = if(it.name != null) viewModel.callApiTranslate(""+viewModel.locale, it.name) else ""
                            val descChanged: String = if(it.description != null) viewModel.callApiTranslate(""+viewModel.locale, it.description) else ""

                            apply {
                                it.name = nameChanged
                                it.description = descChanged
                            }
                        }
                        results.addAll(itemStateTemp)
                        viewModel.adapter.addAllSearch(results)
                        viewModel.hide()

                        if (viewModel.adapter.itemCount > 0) {
                            binding.idDataNotFound.root.visibility = View.GONE
                        } else {
                            binding.idDataNotFound.root.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                val itemStateTemp = changeValue
                results.addAll(itemStateTemp)
                viewModel.adapter.addAllSearch(results)
                viewModel.hide()
            }


            totalPages = it.meta?.total_pages!!
            if (currentPage == totalPages) {
                viewModel.adapter.removeLoadingFooter()
            } else if (currentPage <= totalPages) {
                viewModel.adapter.addLoadingFooter()
                isLastPage = false
            } else {
                isLastPage = true
            }
        })


        viewModel.itemLiveNoticeSecond.observe(viewLifecycleOwner, Observer {
            viewModel.show()
            val typeToken = object : TypeToken<List<ItemLiveNotice>>() {}.type
            val changeValue =
                Gson().fromJson<List<ItemLiveNotice>>(Gson().toJson(it.data), typeToken)
            if (IS_LANGUAGE){
                if (MainActivity.context.get()!!
                        .getString(R.string.englishVal) == "" + viewModel.locale
                ) {
                    val itemStateTemp = changeValue
                    results.addAll(itemStateTemp)
                    viewModel.adapter.addAllSearch(results)
                    viewModel.hide()
                } else {
                    val itemStateTemp = changeValue
                    mainThread {
                        itemStateTemp.forEach {
                            delay(50)
                            val nameChanged: String = if(it.name != null) viewModel.callApiTranslate(""+viewModel.locale, it.name) else ""
                            val descChanged: String = if(it.description != null) viewModel.callApiTranslate(""+viewModel.locale, it.description) else ""

                            apply {
                                it.name = nameChanged
                                it.description = descChanged
                            }
                        }
                        results.addAll(itemStateTemp)
                        viewModel.adapter.addAllSearch(results)
                        viewModel.hide()
                    }
                }
            } else {
                val itemStateTemp = changeValue
                results.addAll(itemStateTemp)
                viewModel.adapter.addAllSearch(results)
                viewModel.hide()
            }


            viewModel.adapter.removeLoadingFooter()
            isLoading = false
            viewModel.adapter.addAllSearch(results)
            if (currentPage != totalPages) viewModel.adapter.addLoadingFooter()
            else isLastPage = true
        })




        viewModel.counterNetwork.observe(viewLifecycleOwner, Observer {
            if (it) {
                if(networkCount == 1){
                    if(networkAlert?.isShowing == true) {
                        return@Observer
                    }
                    val dialogBinding = DialogBottomNetworkBinding.inflate(requireContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    )
                    networkAlert = BottomSheetDialog(requireContext())
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
                            if(totalPages == 1){
                                loadFirstPage()
                            } else {
                                loadNextPage()
                            }
                            networkCount = 1
                        }
                    }
                }
                networkCount++
            }
        })
    }



//    override fun onDestroyView() {
//        _binding = null
//        super.onDestroyView()
//    }
}