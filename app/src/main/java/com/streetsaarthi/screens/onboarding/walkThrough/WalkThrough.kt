package com.streetsaarthi.screens.onboarding.walkThrough

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.streetsaarthi.R
import com.streetsaarthi.databinding.WalkThroughBinding
import com.streetsaarthi.screens.onboarding.quickRegistration.QuickRegistration1
import com.streetsaarthi.screens.onboarding.quickRegistration.QuickRegistration2
import com.streetsaarthi.utils.mainThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class WalkThrough : Fragment() {
    private var _binding: WalkThroughBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WalkThroughVM by viewModels()

    var tabPosition: Int = 0;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WalkThroughBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            var adapter= WalkThroughPagerAdapter()
            adapter.submitList(viewModel.itemMain)
            adapter.notifyDataSetChanged()
            introViewPager.adapter=adapter
            TabLayoutMediator(tabLayout, introViewPager) { tab, position ->
            }.attach()


            introViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.e("Selected_Page", position.toString())
                    tabPosition = position
//                    if(position == 2) {
//                        mainThread {
//                            delay(1000)
//                            requireView().findNavController().navigate(R.id.action_walkThrough_to_onBoard)
//                        }
//                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
        }


        binding.btSignIn.setOnClickListener {
            if (tabPosition == 0){
                binding.introViewPager.setCurrentItem(1, false)
            } else if (tabPosition == 1){
                binding.introViewPager.setCurrentItem(2, false)
            } else {
                requireView().findNavController().navigate(R.id.action_walkThrough_to_onBoard)
            }
        }


//        binding.btSignUp.setOnClickListener {
//            requireView().findNavController().navigate(R.id.action_walkThrough_to_Registar)
//        }

    }


    data class Item (
        var name: String = "",
        var desc: String = "",
        var image: Int = 0
    )


}
