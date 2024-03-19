package com.streetsaarthi.nasvi.screens.main.scheme

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezatpanah.hilt_retrofit_paging_youtube.adapter.LoadMoreAdapter
import com.ezatpanah.hilt_retrofit_paging_youtube.adapter.MoviesAdapter
import com.ezatpanah.hilt_retrofit_paging_youtube.viewmodel.MoviesViewModel
import com.google.gson.Gson
import com.streetsaarthi.nasvi.databinding.LiveSchemesBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.screens.main.schemes.liveSchemes.LiveSchemesVM
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.ioDispatcher
import com.streetsaarthi.nasvi.utils.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class Scheme : Fragment() {
    private val viewModel: MoviesViewModel by viewModels()
    private var _binding: LiveSchemesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LiveSchemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleScope.launch {
                viewModel.getMovie(JSONObject().apply {
                //    put("page", 1)
                    put("user_id", "1044")
                }).collect {
                    Log.e("TAG", "getMovie "+it.toString())
                    moviesAdapter.submitData(it)
                }
            }


            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moviesAdapter
            }

            recyclerView.adapter=moviesAdapter.withLoadStateFooter(
                LoadMoreAdapter{
                    moviesAdapter.retry()
                }
            )



            lifecycleScope.launch {
                moviesAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    prgBarMovies.isVisible = state is LoadState.Loading
                }
            }

            moviesAdapter.setOnItemClickListener {
                it.apply {
                    name = "aaaaaaaaaa"
                }
                moviesAdapter.notifyDataSetChanged()
            }

//
//            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
//                lifecycleScope.launch {
//                    if (loginUser != null) {
//                        val obj: JSONObject = JSONObject().apply {
//                            put("page", 1)
//                            put( "search_input", binding.inclideHeaderSearch.editTextSearch.text.toString())
//                            put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
//                        }
//                        if (requireContext().isNetworkAvailable()) {
//                            idNetworkNotFound.root.visibility = View.GONE
//                            viewModel.getMovie(obj).collect {
//                                Log.e("TAG", "getMovie "+it.toString())
//                                moviesAdapter.submitData(it)
//                            }
//                        } else {
//                            idNetworkNotFound.root.visibility = View.VISIBLE
//                        }
//                    }
//
//                }
//            }




//            lifecycleScope.launch {
//                viewLifecycleOwner.lifecycle.withStarted {
//
//                }
//
//
//                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
//                    if (loginUser != null) {
//                        val obj: JSONObject = JSONObject().apply {
//                            put("page", 1)
//                            put(
//                                "search_input",
//                                binding.inclideHeaderSearch.editTextSearch.text.toString()
//                            )
//                            put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
//                        }
//
//
//                        if (requireContext().isNetworkAvailable()) {
//
//                            CoroutineScope(Dispatchers.IO + Job()).launch {
//                                viewModel.getMovie(obj).collect{
//                                    moviesAdapter.submitData(it)
//                                }
//                            }
//                            binding.idNetworkNotFound.root.visibility = View.GONE
//                        } else {
//                            binding.idNetworkNotFound.root.visibility = View.VISIBLE
//                        }
//                    }
//                }
//
////                viewModel.getMovie(JSONObject()).collect{
////                    moviesAdapter.submitData(it)
////                }
//            }

//
//            lifecycleScope.launch {
//                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
//                    if (loginUser != null) {
//                        val obj: JSONObject = JSONObject().apply {
//                            put("page", 1)
//                            put(
//                                "search_input",
//                                binding.inclideHeaderSearch.editTextSearch.text.toString()
//                            )
//                            put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
//                        }
//
//
//
//                        if (requireContext().isNetworkAvailable()) {
//                            CoroutineScope(Dispatchers.IO + Job()).launch {
//                                viewModel.getMovie(obj).collect{
//                                    moviesAdapter.submitData(it)
//                                }
//                            }
//                            binding.idNetworkNotFound.root.visibility = View.GONE
//                        } else {
//                            binding.idNetworkNotFound.root.visibility = View.VISIBLE
//                        }
//                    }
//                }
//
//                withStarted {
//
//
//
//                }
//            }



        }
    }

}