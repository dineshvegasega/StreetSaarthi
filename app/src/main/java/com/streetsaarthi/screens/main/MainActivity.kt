package com.streetsaarthi.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.networking.Screen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.streetsaarthi.R
import com.streetsaarthi.databinding.MainActivityBinding
import com.streetsaarthi.datastore.DataStoreKeys
import com.streetsaarthi.datastore.DataStoreUtil
import com.streetsaarthi.datastore.DataStoreUtil.readData
import com.streetsaarthi.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        lateinit var context: WeakReference<Context>

        @JvmStatic
        lateinit var activity:  WeakReference<Activity>

        @JvmStatic
        lateinit var mainActivity: WeakReference<MainActivity>

        var logoutAlert : AlertDialog?= null

        @SuppressLint("StaticFieldLeak")
        var navHostFragment : NavHostFragment? = null

        private var _binding: MainActivityBinding? = null
        val binding get() = _binding!!
    }



    private val viewModel: MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = WeakReference(this)
        activity = WeakReference(this)
        mainActivity = WeakReference(this)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navHostFragment!!.navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
//            override fun onDestinationChanged(
//                controller: NavController,
//                destination: NavDestination,
//                arguments: Bundle?
//            ) {
//                Log.e("TAG", "onDestinationChanged: " + destination.label)
//            }
//        })

        if (intent!!.hasExtra(Screen)){
            var screen = intent.getStringExtra(Screen)
            Log.e("TAG", "screen "+screen)
            navHostFragment?.navController?.navigate(R.id.action_splash_to_start)
        }


        binding.btLogout.setOnClickListener {
            // MainActivity.mainActivity.get()!!.callBack()
            if(logoutAlert?.isShowing == true) {
                return@setOnClickListener
            }
            logoutAlert = MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
                .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                    dialog.dismiss()
                    DataStoreUtil.removeKey(DataStoreKeys.LOGIN_DATA) {}
                    DataStoreUtil.removeKey(DataStoreKeys.AUTH) {}
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.drawerLayout.close()
                    }, 500)

                    callBack()
                    val navOptions: NavOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_bar, true)
                        .build()
                    navHostFragment?.navController?.navigate(R.id.start, null, navOptions)
                }
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.drawerLayout.close()
                    }, 500)
                }
                .setCancelable(false)
                .show()
        }


//        binding.btLogout.setOnClickListener {
//            MainActivity.mainActivity.get()!!.callBack()
//
//            var logoutAlert = MaterialAlertDialogBuilder(this)
//                .setTitle(resources.getString(R.string.logout))
//                .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
//                .setPositiveButton(resources.getString(R.string.yes)) { dialog, i ->
//                    dialog.dismiss()
//
//                    CallDataStore.clearAllData()
//                    Toast.makeText(requireContext(), getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show()
//                    view.navigateDirection(SettingsDirections.actionSettings2ToLogin())
//                }
//                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, i ->
//                    dialog.dismiss()
//                }
//                .setCancelable(false)
//                .show()
//
//
//        }


        val mDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.open, R.string.close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                Log.e("TAG", "onDrawerClosed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                Log.e("TAG", "onDrawerOpened")
            }
        }
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        binding.apply {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            textTitleMain.setOnClickListener {
                drawerLayout.close()
            }

            topLayout.ivMenu.setOnClickListener {
                drawerLayout.open()
            }

            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.menuAdapter
            viewModel.menuAdapter.submitList(viewModel.itemMain)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }



//         onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
////                Log.e("TAG", "onBackPressedDispatcher")
//                //view?.navigateBack()
////                if (binding.webView.canGoBack())
////                    binding.webView.goBack()
////                else
////                    view?.navigateBack()
////                    //view?.
//
//                var cc = navHostFragment!!.getChildFragmentManager().getFragments().get(0);
//                Log.e("TAG", "onBackPressedDispatcher "+cc)
//            }
//        })


//        if(intent != null){
//            var screen = intent.getStringExtra(Screen)
//            when(screen){
//                Start -> navHostFragment.navController.navigate(R.id.start)
//                else -> print("I don't know anything about it")
//            }
//        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("TAG", "onNewIntent "+intent)
        //navHostFragment!!.navController.navigate(R.id.start)
        if (intent!!.hasExtra(Screen)){
            var screen = intent.getStringExtra(Screen)
            Log.e("TAG", "screen "+screen)
            navHostFragment?.navController?.navigate(R.id.start)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,""))
    }

    fun callBack() {
        binding.apply {
            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser == null) {
                    binding.topLayout.topToolbar.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                } else {
                    binding.topLayout.topToolbar.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
    }
}
