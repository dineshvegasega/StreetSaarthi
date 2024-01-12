package com.streetsaarthi.nasvi.screens.main.dashboard

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DashboardBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.OtpTimer
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


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = viewModel.dashboardAdapter
        viewModel.isScheme.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.itemMain?.get(1)?.apply {
                    isNew = true
                }
            }
        })
        viewModel.isNotice.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.itemMain?.get(2)?.apply {
                    isNew = true
                }
            }
        })
        viewModel.isTraining.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.itemMain?.get(3)?.apply {
                    isNew = true
                }
            }
        })
        viewModel.dashboardAdapter.notifyDataSetChanged()
        viewModel.dashboardAdapter.submitList(viewModel.itemMain)

        DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                val obj: JSONObject = JSONObject().apply {
                    put("page", "1")
                    put("status", "Active")
//                    put("search_input", USER_TYPE)
                    put("user_id", Gson().fromJson(loginUser, Login::class.java).id)
                }
//                viewModel.liveScheme(view = requireView(), obj)
//                viewModel.liveTraining(view = requireView(), obj)
//                viewModel.liveNotice(view = requireView(), obj)
            }
        }



//
//        binding.recyclerViewRecent.setHasFixedSize(true)
//        binding.recyclerViewRecent.adapter = viewModel.recentAdapter
//        viewModel.recentAdapter.notifyDataSetChanged()
//        viewModel.recentAdapter.submitList(viewModel.itemMain)


//        viewModel.readResult.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                itemMain = it?.items!!
//                viewModel.photosAdapter.submitList(itemMain)
//                if(itemMain?.isEmpty() == true){
//                    binding.txtMsg.visibility = View.VISIBLE
//                }else{
//                    binding.txtMsg.visibility = View.GONE
//                }
//            }
//            viewModel.photosAdapter.notifyDataSetChanged()
//        })

//        viewModel.getProducts("a")

    }


//    // Creating menus
//    private fun createMenu(menuHost: MenuHost) {
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.home_menu,menu)
//                var search = (menu?.findItem(R.id.search)?.actionView as SearchView)
//                search.setOnQueryTextListener(object : OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                       // viewModel.getProducts(query)
//                        return false
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        return false
//                    }
//                })
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return when(menuItem.itemId){
//                    R.id.search->{
//                        true
//                    }
//                    else->{
//                        false
//                    }
//                }
//            }
//        },viewLifecycleOwner, Lifecycle.State.RESUMED)
//    }
//


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}