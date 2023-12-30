package com.streetsaarthi.screens.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.streetsaarthi.R
import com.streetsaarthi.databinding.HomeBinding
import com.streetsaarthi.models.Item
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {
    private val viewModel: HomeVM by viewModels()
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeBinding.inflate(inflater)
        return binding.root
//        val view = inflater.inflate(R.layout.home, container, false)
//        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val menuHost: MenuHost = requireActivity()
        //createMenu(menuHost)
//        binding.recyclerView.setHasFixedSize(true)
//        binding.recyclerView.adapter = viewModel.photosAdapter
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



}