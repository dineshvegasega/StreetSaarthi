package com.streetsaarthi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.demo.networking.Screen
import com.demo.networking.Start
import com.streetsaarthi.databinding.MainActivityBinding
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
    }
    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!
    var navHostFragment : NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setContentView(R.layout.main_activity)

        context = WeakReference(this)
        activity = WeakReference(this)

       // navController = findNavController(R.id.nav_host_fragment)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //navHostFragment.navController!!.navigate(R.id.start)


        if (intent!!.hasExtra(Screen)){
            var screen = intent.getStringExtra(Screen)
            Log.e("TAG", "screen "+screen)
            navHostFragment?.navController?.navigate(R.id.action_splash_to_start)
        }

//        val json = JsonObject().apply {
//            addProperty("age","28")
//            addProperty("name","john")
//            addProperty("contents","test")
//        }


//         onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Log.e("TAG", "onBackPressedDispatcher")
//                //view?.navigateBack()
//                if (binding.webView.canGoBack())
//                    binding.webView.goBack()
//                else
//                    view?.navigateBack()
//                    //view?.
//            }
//
//
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

//        if(intent != null){
//            var screen = intent.getStringExtra(Screen)
//            when(screen){
//                Start -> navHostFragment?.navController?.navigate(R.id.start)
//                else -> print("I don't know anything about it")
//            }
//        }

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,""))
    }
}
