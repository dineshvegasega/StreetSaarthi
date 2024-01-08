package com.streetsaarthi.screens.onboarding.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.datastore.DataStoreUtil.readData
import com.streetsaarthi.R
import com.streetsaarthi.databinding.SplashBinding

import com.streetsaarthi.utils.mainThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class Splash : Fragment() {
//    private val viewModel: LoginVM by viewModels()
    private var _binding: SplashBinding? = null
    private val binding get() = _binding!!

//    @Inject
//    lateinit var dataStore: DataStoreUtil



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume2 ")
        MainActivity.hideValue = true
        handleSplashTime()
    }

    private fun handleSplashTime() {
        mainThread {
            delay(2000)
            readData(LOGIN_DATA) { loginUser ->
                if(loginUser == null){
                    requireView().findNavController().navigate(R.id.action_splash_to_start)
                    MainActivity.mainActivity.get()!!.callBack()
                }else{
                    requireView().findNavController().navigate(R.id.action_splash_to_dashboard)
                    MainActivity.mainActivity.get()!!.callBack()
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}