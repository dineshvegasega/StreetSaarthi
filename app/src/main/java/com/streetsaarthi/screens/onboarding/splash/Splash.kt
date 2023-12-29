package com.streetsaarthi.screens.onboarding.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.R
import com.streetsaarthi.databinding.SplashBinding
import com.streetsaarthi.datastore.getUserData

import com.streetsaarthi.utils.mainThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    ): View? {
        _binding = SplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSplashTime()



    }


    private fun handleSplashTime() {
        mainThread {
            delay(2000)
//            dataStore.readData(LOGIN_DATA) {
//                if(it==null){
//                    requireView().findNavController().navigate(R.id.action_splash_to_start)
//                }else{
//                    requireView().findNavController().navigate(R.id.action_splash_to_home)
//                }
//            }

//            if(getUserData{} != null){
//                Log.e("TAG", "thisssAAA "+this)
//            } else {
//                Log.e("TAG", "thisssBBB "+this)
//            }

            getUserData {
                Log.e("TAG", "thisss "+this)
            }





//            getUserData {
//                if ((profileStatus ?: 0) == ProfileStatus.PROFILE_COMPLETE.status)
//                requireView().navigateDirection(SplashDirections.actionSplashToHome())
//                else
////                    requireView().navigateDirection(SplashDirections.actionSplashToSignIn())
//                    requireView().navigateDirection(SplashDirections.actionSplashToWelcome())
//            }
        }
    }

}