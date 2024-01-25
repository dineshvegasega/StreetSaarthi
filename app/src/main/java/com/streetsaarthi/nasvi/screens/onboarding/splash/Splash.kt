package com.streetsaarthi.nasvi.screens.onboarding.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.SplashBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys.LOGIN_DATA
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.screens.main.dashboard.Dashboard
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.navHostFragment
import com.streetsaarthi.nasvi.screens.onboarding.start.Start
import com.streetsaarthi.nasvi.utils.ioThread
import com.streetsaarthi.nasvi.utils.mainThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class Splash : Fragment() {
//    private val viewModel: LoginVM by viewModels()
    private var _binding: SplashBinding? = null
    private val binding get() = _binding!!

//    @Inject
//    lateinit var dataStore: DataStoreUtil

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
        Log.e("TAG", "onResume2 ")
        handleSplashTime()
    }

    private fun handleSplashTime() {
        ioThread {
            delay(2000)
            readData(LOGIN_DATA) { loginUser ->
                var fragmentInFrame = navHostFragment!!.getChildFragmentManager().getFragments().get(0)
                if(loginUser == null){
                    Log.e("TAG", "onResume2AAA ")
                    if (fragmentInFrame !is Start){
                        navHostFragment?.navController?.navigate(R.id.action_splash_to_start)
//                      requireView().findNavController().navigate(R.id.action_splash_to_start)
                        MainActivity.mainActivity.get()!!.callBack()
                    }
                }else{
                    Log.e("TAG", "onResume2BBB ")
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