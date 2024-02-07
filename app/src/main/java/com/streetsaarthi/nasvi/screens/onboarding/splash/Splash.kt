package com.streetsaarthi.nasvi.screens.onboarding.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.SplashBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.screens.main.dashboard.Dashboard
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.navHostFragment
import com.streetsaarthi.nasvi.screens.onboarding.start.Start
import com.streetsaarthi.nasvi.utils.ioThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class Splash : Fragment() {
    private var _binding: SplashBinding? = null
    private val binding get() = _binding!!


var viewOf : View ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewOf = view
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
    }


    override fun onResume() {
        super.onResume()
        handleSplashTime()
    }

    private fun handleSplashTime() {
        ioThread {
            delay(2000)
            readData(LOGIN_DATA) { loginUser ->
                var fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
                if(loginUser == null){
                    if (fragmentInFrame !is Start){
                        navHostFragment?.navController?.navigate(R.id.action_splash_to_start)
//                      requireView().findNavController().navigate(R.id.action_splash_to_start)
                        MainActivity.mainActivity.get()!!.callBack()
                    }
                }else{
                    if (fragmentInFrame !is Dashboard){
                        navHostFragment?.navController?.navigate(R.id.action_splash_to_dashboard)
//                      requireView().findNavController().navigate(R.id.action_splash_to_dashboard)
                        MainActivity.mainActivity.get()!!.callBack()
                   }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}