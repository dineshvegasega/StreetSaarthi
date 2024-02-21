package com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.Repository
import com.streetsaarthi.nasvi.databinding.DialogBottomNetworkBinding
import com.streetsaarthi.nasvi.databinding.LiveSchemesBinding
import com.streetsaarthi.nasvi.databinding.LoaderBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemLiveScheme
import com.streetsaarthi.nasvi.models.mix.ItemState
import com.streetsaarthi.nasvi.networking.ApiTranslateInterface
import com.streetsaarthi.nasvi.networking.new.ApiClient
import com.streetsaarthi.nasvi.screens.main.webPage.WebPage
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.CheckValidation
import com.streetsaarthi.nasvi.utils.PaginationScrollListener
import com.streetsaarthi.nasvi.utils.callUrlAndParseResult
import com.streetsaarthi.nasvi.utils.ioThread
import com.streetsaarthi.nasvi.utils.suspendCallUrlAndParseResult
import com.streetsaarthi.nasvi.utils.mainDispatcher
import com.streetsaarthi.nasvi.utils.mainThread
import com.streetsaarthi.nasvi.utils.onRightDrawableClicked
import com.streetsaarthi.nasvi.utils.parseResult
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject


@AndroidEntryPoint
class LiveSchemes : Fragment() {
    private val viewModel: LiveSchemesVM by viewModels()
    private var _binding: LiveSchemesBinding? = null
    private val binding get() = _binding!!


    companion object {
        var isReadLiveSchemes: Boolean? = false
//        var callBackListener: ListCallBackListener? = null
    }

    var networkAlert: BottomSheetDialog? = null
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
        _binding = LiveSchemesBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        isReadLiveSchemes = true

        binding.apply {

            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.live_schemes)
            idDataNotFound.textDesc.text = getString(R.string.currently_no_schemes)

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

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    inclideHeaderSearch.editTextSearch.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        if (count >= 1) R.drawable.ic_cross_white else R.drawable.ic_search,
                        0
                    );
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
            recyclerView.addOnScrollListener(object :
                PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    currentPage += 1
                    if (totalPages >= currentPage) {
                        Handler(Looper.myLooper()!!).postDelayed({
                            loadNextPage()
                        }, LOADER_TIME)
                    }
                    Log.e("TAG", "currentPage " + currentPage)
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
        pageStart = 1
        isLoading = false
        isLastPage = false
        totalPages = 1
        currentPage = pageStart
        results.clear()
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
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

    fun loadNextPage() {
        Log.e("TAG", "loadNextPage " + currentPage)
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
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


    var results: MutableList<ItemLiveScheme> = ArrayList()

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun observerDataRequest() {
        viewModel.itemLiveSchemes.observe(viewLifecycleOwner, Observer {
            viewModel.show()
            val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
            val changeValue =
                Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(it.data), typeToken)

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
                            val nameChanged: String = viewModel.callApiTranslate(""+viewModel.locale, it.name)
                            val descChanged: String = viewModel.callApiTranslate(""+viewModel.locale, it.description)
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


        viewModel.itemLiveSchemesSecond.observe(requireActivity()) {
            viewModel.show()
            val typeToken = object : TypeToken<List<ItemLiveScheme>>() {}.type
            val changeValue =
                Gson().fromJson<List<ItemLiveScheme>>(Gson().toJson(it.data), typeToken)

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
                        val nameChanged: String = viewModel.callApiTranslate(""+viewModel.locale, it.name)
                        val descChanged: String = viewModel.callApiTranslate(""+viewModel.locale, it.description)
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

            viewModel.adapter.removeLoadingFooter()
            isLoading = false
            viewModel.adapter.addAllSearch(results)
            if (currentPage != totalPages) viewModel.adapter.addLoadingFooter()
            else isLastPage = true
        }


        viewModel.applyLink.observe(requireActivity()) { position ->
            if (position != -1) {
                var data = results.get(position).apply {
                    user_scheme_status = "applied"
                }
                viewModel.adapter.notifyDataSetChanged()
                viewModel.viewDetail(data, position = position, requireView(), 3)
            }
        }




        viewModel.counterNetwork.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (networkCount == 1) {
                    if (networkAlert?.isShowing == true) {
                        return@Observer
                    }
                    val dialogBinding = DialogBottomNetworkBinding.inflate(
                        requireContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE
                        ) as LayoutInflater
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
                            if (totalPages == 1) {
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



//
//    @Throws(Exception::class)
//     suspend fun callUrlAndParseResult(
//        langTo: String,
//        words: String
//    ): String {
//        var myResponse = ""
//        val url = "https://translate.googleapis.com/translate_a/single?" +
//                "client=gtx&" +
//                "sl=" + "en" +
//                "&tl=" + langTo +
//                "&dt=t&q=" + URLEncoder.encode(words, "UTF-8")
//        val httpURLConnection = URL(url).openConnection() as HttpURLConnection
//        httpURLConnection.setRequestProperty("Accept", "application/json")
//        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0")
//        httpURLConnection.requestMethod = "GET"
//        httpURLConnection.doInput = true
//        httpURLConnection.doOutput = false
//        httpURLConnection.connectTimeout = 10000
//        httpURLConnection.readTimeout = 10000
//        val responseCode = httpURLConnection.responseCode
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            val response = httpURLConnection.inputStream.bufferedReader().use {it.readText() }
//            myResponse = response.toString().parseResult()
//        }
//
//        httpURLConnection.inputStream.bufferedReader().use {
//            it.close()
//        }
//        return myResponse
//    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}