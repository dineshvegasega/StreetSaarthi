package com.streetsaarthi.screens.onboarding.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.MainActivity
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

//            getUserData {
//                Log.e("TAG", "thisss "+this)
//            }


//            DataStoreUtil.readDataSynchronously(LANGUAGE){ language ->
//                if(!language.isNullOrEmpty() && language != ENGLISH) {
//                    changeLang(lang = language, restartActivity = false)
//                }
//            }



            readData(LOGIN_DATA) { loginUser ->
                if(loginUser == null){
                    requireView().findNavController().navigate(R.id.action_splash_to_start)
                }else{
                    requireView().findNavController().navigate(R.id.action_splash_to_home)
                }
            }
            MainActivity.mainActivity.get()!!.callBack()

//            DataStoreUtil.readData(AUTH) {
//                token = it ?: ""
//                if(promoCode.get() != null)
//                {
//                    type.set(2)
//                    applyPromoCode()
//                }
//                else
//                {
//                    getCart()
//                }
//            }


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